package dev.logvinovich.inventario.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class SalesByDateDto(
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") val date: LocalDateTime,
    val totalAmount: Double
)

data class WarehouseSalesDto(val warehouseName: String, val totalAmount: Double)

data class ProductSalesDto(val productName: String, val totalAmount: Double)

data class StatsDto(
    val salesByDate: List<SalesByDateDto>,
    val topWarehouses: List<WarehouseSalesDto>,
    val topProducts: List<ProductSalesDto>,
    val totalSalesAmount: Float,
    val totalSalesCount: Int
)