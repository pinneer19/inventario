package dev.logvinovich.inventario.domain.model

import kotlinx.datetime.LocalDateTime

data class SalesByDate(val date: LocalDateTime, val totalAmount: Float)

data class WarehouseSales(val warehouseName: String, val totalAmount: Float)

data class ProductSales(val productName: String, val totalAmount: Float)

data class Stats(
    val salesByDate: List<SalesByDate>,
    val topWarehouses: List<WarehouseSales>,
    val topProducts: List<ProductSales>,
    val totalSalesAmount: Float,
    val totalSalesCount: Int
)