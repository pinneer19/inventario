package dev.logvinovich.inventario.main.warehouse.viewmodel

import dev.logvinovich.inventario.domain.model.InventoryItem

sealed interface WarehouseIntent {
    data class GetInventoryItems(val warehouseId: Long) : WarehouseIntent

    data object FilterItems : WarehouseIntent

    data object ClearSearchText : WarehouseIntent

    data class UpdateSearchText(val text: String) : WarehouseIntent

    data class DeleteItem(val itemId: Long) : WarehouseIntent

    data class SetSavedItem(val item: InventoryItem) : WarehouseIntent
}