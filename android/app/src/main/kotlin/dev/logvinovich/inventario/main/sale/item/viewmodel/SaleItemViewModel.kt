package dev.logvinovich.inventario.main.sale.item.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.domain.model.SaleItem
import dev.logvinovich.inventario.domain.usecase.sale.AddSaleUseCase
import dev.logvinovich.inventario.domain.usecase.warehouse.item.GetWarehouseItemsUseCase
import dev.logvinovich.inventario.ui.util.SnackbarController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SaleItemViewModel.Factory::class)
class SaleItemViewModel @AssistedInject constructor(
    @Assisted private val warehouseId: Long,
    private val getWarehouseItemsUseCase: GetWarehouseItemsUseCase,
    private val addSaleUseCase: AddSaleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaleItemUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInventoryItems()
    }

    fun handleIntent(intent: SaleItemIntent) {
        when (intent) {
            SaleItemIntent.AddItemToSale -> addSaleItem()

            SaleItemIntent.SubmitSale -> submitSale()

            is SaleItemIntent.RemoveItem -> removeSaleItem(intent.item)

            is SaleItemIntent.SelectInventoryItem -> updateSelectedIndex(intent.index)

            is SaleItemIntent.UpdatePrice -> updatePrice(intent.value)

            is SaleItemIntent.UpdateQuantity -> updateQuantity(intent.value)
        }
    }

    private fun loadInventoryItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result = getWarehouseItemsUseCase(warehouseId)

            if (result.isSuccess) {
                _uiState.update { it.copy(availableItems = result.getOrThrow(), loading = false) }
            } else {
                _uiState.update { it.copy(loading = false) }
                SnackbarController.sendMessageResEvent(R.string.load_items_failed)
            }
        }
    }

    private fun addSaleItem() {
        if (!validateSaleInputs()) return

        val updatedSaleItems = with(_uiState.value) {
            val selectedItem = availableItems[requireNotNull(selectedItemIndex)]

            val existingItem =
                saleItems.firstOrNull { it.inventoryItemId == selectedItem.id }

            if (selectedItem.quantity < quantity.toInt()) {
                _uiState.update { it.copy(quantityError = true) }
                return
            }

            existingItem?.let {
                val updatedItem = it.copy(
                    quantity = it.quantity + quantity.toInt(),
                    price = it.price + price.toFloat()
                )
                saleItems.toMutableList().apply {
                    set(selectedItemIndex, updatedItem)
                }.toList()
            } ?: (saleItems + SaleItem(
                id = null,
                inventoryItemId = selectedItem.id,
                quantity = quantity.toInt(),
                price = price.toFloat(),
                productName = selectedItem.product.name
            ))
        }

        _uiState.update {
            it.copy(
                saleItems = updatedSaleItems,
                selectedItemIndex = null,
                quantity = "",
                price = ""
            )
        }
    }

    private fun submitSale() {
        viewModelScope.launch {
            if (_uiState.value.saleItems.isEmpty()) {
                SnackbarController.sendMessageResEvent(R.string.empty_sale)
                return@launch
            }

            _uiState.update { it.copy(loading = true) }

            val result = addSaleUseCase(warehouseId, _uiState.value.saleItems)

            if (result.isSuccess) {
                _uiState.update { it.copy(loading = false, saleCompleted = true) }
                SnackbarController.sendMessageResEvent(R.string.sale_was_saved_successfully)
            } else {
                _uiState.update { it.copy(loading = false) }
                SnackbarController.sendMessageResEvent(R.string.sale_not_saved)
            }
        }
    }

    private fun validateSaleInputs(): Boolean {
        val quantity = _uiState.value.quantity.toIntOrNull()
        val price = _uiState.value.price.toFloatOrNull()

        if (quantity == null) {
            _uiState.update { it.copy(quantityError = true) }
            return false
        } else _uiState.update { it.copy(quantityError = false) }
        if (price == null) {
            _uiState.update { it.copy(priceError = true) }
            return false
        } else _uiState.update { it.copy(priceError = false) }

        if (_uiState.value.selectedItemIndex == null) {
            viewModelScope.launch {
                SnackbarController.sendMessageResEvent(R.string.must_select_item)
            }
            return false
        }

        return true
    }

    private fun removeSaleItem(item: SaleItem) {
        _uiState.update {
            it.copy(saleItems = it.saleItems - item)
        }
    }

    private fun updateSelectedIndex(index: Int) {
        _uiState.update { state ->
            with(state.availableItems[index]) {
                state.copy(
                    selectedItemIndex = index,
                    quantity = quantity.toString(),
                    price = price.toString(),
                    availableQuantity = quantity.toString()
                )
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

    @AssistedFactory
    interface Factory {
        fun create(warehouseId: Long): SaleItemViewModel
    }
}