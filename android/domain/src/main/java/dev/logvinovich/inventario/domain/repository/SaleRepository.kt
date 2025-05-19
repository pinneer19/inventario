package dev.logvinovich.inventario.domain.repository

import dev.logvinovich.inventario.domain.model.Sale
import dev.logvinovich.inventario.domain.model.SaleItem
import dev.logvinovich.inventario.domain.model.Stats
import kotlinx.datetime.LocalDate

interface SaleRepository {
    suspend fun addSale(warehouseId: Long, items: List<SaleItem>): Result<Sale>

    suspend fun getSales(
        warehouseId: Long?,
        fromDate: LocalDate?,
        toDate: LocalDate?
    ): Result<List<Sale>>

    suspend fun deleteSale(saleId: Long): Result<Unit>

    suspend fun getStats(): Result<Stats>
}