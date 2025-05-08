package dev.logvinovich.inventario.main.warehouse.viewmodel

sealed interface WarehouseIntent {
    data object GetInventoryItems : WarehouseIntent
}