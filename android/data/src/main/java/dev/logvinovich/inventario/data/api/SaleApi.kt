package dev.logvinovich.inventario.data.api

import dev.logvinovich.inventario.data.model.sale.SaleDto
import dev.logvinovich.inventario.data.model.sale.SaleItemDto
import dev.logvinovich.inventario.data.model.sale.StatsDto
import dev.logvinovich.inventario.data.util.apiRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.datetime.LocalDate

suspend inline fun HttpClient.addSale(
    warehouseId: Long,
    items: List<SaleItemDto>
): Result<SaleDto> {
    return apiRequest {
        post("/sales") {
            parameter("warehouseId", warehouseId)
            setBody(items)
        }
    }
}

suspend inline fun HttpClient.getSales(
    warehouseId: Long?,
    fromDate: LocalDate?,
    toDate: LocalDate?
): Result<List<SaleDto>> {
    return apiRequest {
        get("/sales") {
            parameter("warehouseId", warehouseId)
            parameter("fromDate", fromDate)
            parameter("toDate", toDate)
        }
    }
}

suspend inline fun HttpClient.deleteSale(saleId: Long): Result<Unit> {
    return apiRequest {
        delete("/sales/$saleId")
    }
}

suspend inline fun HttpClient.getSaleStats(): Result<StatsDto> {
    return apiRequest {
        get("/sales/statistics")
    }
}