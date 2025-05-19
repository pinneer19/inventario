package dev.logvinovich.inventario.data.model.sale

import dev.logvinovich.inventario.domain.model.ProductSales
import dev.logvinovich.inventario.domain.model.SalesByDate
import dev.logvinovich.inventario.domain.model.Stats
import dev.logvinovich.inventario.domain.model.WarehouseSales
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class SalesByDateDto(
    val date: LocalDateTime,
    val totalAmount: Float
) {
    fun toSalesByDate() = SalesByDate(
        date = date,
        totalAmount = totalAmount
    )
}

@Serializable
data class WarehouseSalesDto(val warehouseName: String, val totalAmount: Float) {
    fun toWarehouseSales() = WarehouseSales(
        warehouseName = warehouseName,
        totalAmount = totalAmount
    )
}

@Serializable
data class ProductSalesDto(val productName: String, val totalAmount: Float) {
    fun toProductSales() = ProductSales(
        productName = productName,
        totalAmount = totalAmount
    )
}

@Serializable
data class StatsDto(
    val salesByDate: List<SalesByDateDto>,
    val topWarehouses: List<WarehouseSalesDto>,
    val topProducts: List<ProductSalesDto>,
    val totalSalesAmount: Float,
    val totalSalesCount: Int
) {
    fun toStats() = Stats(
        salesByDate = salesByDate.map { it.toSalesByDate() },
        topWarehouses = topWarehouses.map { it.toWarehouseSales() },
        topProducts = topProducts.map { it.toProductSales() },
        totalSalesAmount = totalSalesAmount,
        totalSalesCount = totalSalesCount,
    )
}