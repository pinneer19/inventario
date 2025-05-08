package dev.logvinovich.inventario.main.warehouse.viewmodel

import dev.logvinovich.domain.model.InventoryItem
import dev.logvinovich.inventario.ui.util.UiText

data class WarehouseUiState(
    val loading: Boolean = false,
    val inventoryItems: List<InventoryItem> = emptyList(),
    val uiMessage: UiText? = null
)