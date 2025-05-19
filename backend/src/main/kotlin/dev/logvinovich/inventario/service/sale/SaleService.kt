package dev.logvinovich.inventario.service.sale

import dev.logvinovich.inventario.model.SaleDto
import dev.logvinovich.inventario.model.SaleItemDto
import dev.logvinovich.inventario.model.ServiceResult
import dev.logvinovich.inventario.model.StatsDto
import java.time.LocalDate

interface SaleService {
    fun createSale(warehouseId: Long, items: List<SaleItemDto>): SaleDto?

    fun getSales(warehouseId: Long?, fromDate: LocalDate?, toDate: LocalDate?): List<SaleDto>

    fun getSalesByManagerId(managerId: Long): List<SaleDto>

    fun getSaleById(id: Long): SaleDto?

    fun deleteSale(id: Long): ServiceResult<Unit>

    fun updateSale(id: Long, items: List<SaleItemDto>): ServiceResult<SaleDto>

    fun getStatistics(): StatsDto
}