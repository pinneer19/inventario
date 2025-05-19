package dev.logvinovich.inventario.main.sale.viewmodel

sealed interface SalesIntent {
    data object GetSales : SalesIntent
    data class SetDateRangeFilter(val dateRange: Pair<Long?, Long?>) : SalesIntent
    data class SetWarehouseFilter(val warehouseId: Long?) : SalesIntent
    data class DeleteSale(val saleId: Long) : SalesIntent
}