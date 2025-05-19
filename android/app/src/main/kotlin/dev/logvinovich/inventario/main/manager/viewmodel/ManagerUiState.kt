package dev.logvinovich.inventario.main.manager.viewmodel

import dev.logvinovich.inventario.domain.model.Warehouse

data class ManagerUiState(
    val loading: Boolean = false,
    val warehouses: List<Warehouse> = emptyList(),
    val selectedWarehouseId: Long? = null,
)
