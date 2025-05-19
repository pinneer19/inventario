package dev.logvinovich.inventario.data.repository

import dev.logvinovich.inventario.data.api.addSale
import dev.logvinovich.inventario.data.api.deleteSale
import dev.logvinovich.inventario.data.api.getSaleStats
import dev.logvinovich.inventario.data.api.getSales
import dev.logvinovich.inventario.data.model.sale.toDto
import dev.logvinovich.inventario.domain.model.Sale
import dev.logvinovich.inventario.domain.model.SaleItem
import dev.logvinovich.inventario.domain.model.Stats
import dev.logvinovich.inventario.domain.repository.SaleRepository
import io.ktor.client.HttpClient
import jakarta.inject.Inject
import kotlinx.datetime.LocalDate

class SaleRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : SaleRepository {
    override suspend fun addSale(
        warehouseId: Long,
        items: List<SaleItem>
    ): Result<Sale> {
        return httpClient.addSale(warehouseId, items.map { it.toDto() }).map {
            it.toSale()
        }
    }

    override suspend fun getSales(
        warehouseId: Long?,
        fromDate: LocalDate?,
        toDate: LocalDate?
    ): Result<List<Sale>> {
        return httpClient.getSales(warehouseId, fromDate, toDate).map { dtoList ->
            dtoList.map { it.toSale() }
        }
    }

    override suspend fun deleteSale(saleId: Long): Result<Unit> {
        return httpClient.deleteSale(saleId)
    }

    override suspend fun getStats(): Result<Stats> {
        return httpClient.getSaleStats().map {
            it.toStats()
        }
    }
}