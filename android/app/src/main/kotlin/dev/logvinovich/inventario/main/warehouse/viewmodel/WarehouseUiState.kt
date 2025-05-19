package dev.logvinovich.inventario.main.warehouse.viewmodel

import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.ui.util.UiText

data class WarehouseUiState(
    val loading: Boolean = false,
    val warehouseId: Long = -1,
    val inventoryItems: List<InventoryItem> = emptyList(),
    val filteredInventoryItems: List<InventoryItem> = emptyList(),
    val searchText: String = "",
    val uiMessage: UiText? = null
)