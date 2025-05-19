package dev.logvinovich.inventario.main.sale.viewmodel

import dev.logvinovich.inventario.domain.model.Sale
import dev.logvinovich.inventario.domain.model.Warehouse
import kotlinx.datetime.LocalDate

data class SalesUiState(
    val loading: Boolean = false,
    val warehouses: List<Warehouse> = emptyList(),
    val selectedWarehouseId: Long? = null,
    val initialDataIsEmpty: Boolean = false,
    val dateRange: Pair<Long?, Long?> = null to null,
    val groupedSales: Map<LocalDate, List<Sale>> = emptyMap()
)