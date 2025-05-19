package dev.logvinovich.inventario.main.warehouse.item.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.domain.usecase.product.GetProductsUseCase
import dev.logvinovich.inventario.domain.usecase.warehouse.item.SaveInventoryItemUseCase
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.ui.util.SnackbarController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = InventoryItemViewModel.Factory::class)
class InventoryItemViewModel @AssistedInject constructor(
    @Assisted private val inventoryItem: InventoryItem?,
    @Assisted private val warehouseId: Long,
    private val saveInventoryItemUseCase: SaveInventoryItemUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        inventoryItem?.toItemUiState() ?: InventoryItemUiState()
    )
    val uiState = _uiState.asStateFlow()

    init {
        getOrganizationProducts()
    }

    fun handleIntent(intent: InventoryItemIntent) {
        when (intent) {
            is InventoryItemIntent.UpdatePrice -> updatePrice(intent.price)
            is InventoryItemIntent.UpdateQuantity -> updateQuantity(intent.quantity)
            is InventoryItemIntent.UpdateSelectedIndex -> updateSelectedIndex(intent.index)
            InventoryItemIntent.SaveItem -> saveItem()
        }
    }

    private fun getOrganizationProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result = getProductsUseCase.invokeWithWarehouseId(warehouseId)

            if (result.isSuccess) {
                _uiState.update {
                    inventoryItem?.let { item ->
                        it.copy(
                            loading = false,
                            organizationProducts = result.getOrThrow(),
                            selectedProductIndex = result.getOrThrow()
                                .indexOfFirst { product -> product.id == item.product.id }
                        )
                    } ?: it.copy(loading = false, organizationProducts = result.getOrThrow())
                }
            } else {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun updatePrice(price: String) {
        _uiState.update { it.copy(price = price) }
    }

    private fun updateQuantity(quantity: String) {
        _uiState.update {
            it.copy(quantity = quantity)
        }
    }

    private fun updateSelectedIndex(index: Int) {
        _uiState.update {
            it.copy(selectedProductIndex = index)
        }
    }

    private fun saveItem() {
        _uiState.update { it.copy(priceError = false, quantityError = false) }

        val itemPrice = _uiState.value.price.toFloatOrNull()
        val itemQuantity = _uiState.value.quantity.toIntOrNull()

        if (itemPrice == null || itemPrice < 0) {
            _uiState.update { it.copy(priceError = true) }
            return
        }

        if (itemQuantity == null || itemQuantity < 0) {
            _uiState.update { it.copy(quantityError = true) }
            return
        }

        if (_uiState.value.selectedProductIndex == null) {
            viewModelScope.launch {
                SnackbarController.sendMessageResEvent(R.string.must_select_product)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            with(_uiState.value) {
                val result = saveInventoryItemUseCase(
                    inventoryItem?.id,
                    organizationProducts[requireNotNull(selectedProductIndex)].id,
                    warehouseId,
                    itemPrice,
                    itemQuantity
                )

                if (result.isSuccess) {
                    _uiState.update { it.copy(loading = false, saveResult = result.getOrThrow()) }
                    SnackbarController.sendMessageResEvent(R.string.item_saved)
                } else {
                    _uiState.update { it.copy(loading = false) }
                    SnackbarController.sendMessageResEvent(R.string.item_save_failed)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(inventoryItem: InventoryItem?, warehouseId: Long): InventoryItemViewModel
    }
}