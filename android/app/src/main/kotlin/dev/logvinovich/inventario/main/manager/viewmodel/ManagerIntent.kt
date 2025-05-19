package dev.logvinovich.inventario.main.manager.viewmodel

interface ManagerIntent {
    data class UpdateSelectedWarehouse(val warehouseId: Long): ManagerIntent

    data object GetWarehouses : ManagerIntent

    data object Logout : ManagerIntent
}