package dev.logvinovich.inventario.main.warehouse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.domain.usecase.warehouse.item.DeleteInventoryItemUseCase
import dev.logvinovich.inventario.domain.usecase.warehouse.item.GetWarehouseItemsUseCase
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.ui.util.SnackbarController
import dev.logvinovich.inventario.ui.util.UiText
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class WarehouseViewModel @Inject constructor(
    private val getWarehouseItemsUseCase: GetWarehouseItemsUseCase,
    private val deleteInventoryItemUseCase: DeleteInventoryItemUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WarehouseUiState())
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: WarehouseIntent) {
        when (intent) {
            is WarehouseIntent.GetInventoryItems -> getInventoryItems(intent.warehouseId)

            WarehouseIntent.FilterItems -> filterItems()

            WarehouseIntent.ClearSearchText -> updateSearchText("")

            is WarehouseIntent.UpdateSearchText -> updateSearchText(intent.text)

            is WarehouseIntent.DeleteItem -> deleteInventoryItem(intent.itemId)

            is WarehouseIntent.SetSavedItem -> setSavedItem(intent.item)
        }
    }

    private fun getInventoryItems(warehouseId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result = getWarehouseItemsUseCase(warehouseId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        loading = false,
                        inventoryItems = result.getOrThrow(),
                        filteredInventoryItems = result.getOrThrow()
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        loading = false,
                        uiMessage = UiText.Plain(result.exceptionOrNull()?.message.orEmpty()),
                    )
                }
            }
        }
    }

    private fun filterItems() {
        _uiState.update {
            it.copy(filteredInventoryItems = it.inventoryItems.filter { item ->
                item.product.name.contains(it.searchText, ignoreCase = true)
            })
        }
    }

    private fun updateSearchText(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }

    private fun deleteInventoryItem(itemId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result = deleteInventoryItemUseCase(itemId)

            if (result.isSuccess) {
                _uiState.update {
                    val deletedItem = requireNotNull(
                        it.inventoryItems.find { item -> item.id == itemId }
                    )
                    it.copy(
                        inventoryItems = it.inventoryItems - deletedItem,
                        filteredInventoryItems = it.filteredInventoryItems - deletedItem,
                        loading = false
                    )
                }
                SnackbarController.sendMessageResEvent(R.string.item_delete_success)
            } else {
                SnackbarController.sendMessageResEvent(R.string.item_delete_fail)
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun setSavedItem(item: InventoryItem) {
        _uiState.update { currentState ->
            val itemExists = currentState.filteredInventoryItems.any { it.id == item.id }
            if (itemExists) {
                val updatedItems = currentState.filteredInventoryItems.map {
                    if (it.id == item.id) item else it
                }
                currentState.copy(filteredInventoryItems = updatedItems)
            } else {
                currentState.copy(filteredInventoryItems = currentState.filteredInventoryItems + item)
            }
        }
    }
}