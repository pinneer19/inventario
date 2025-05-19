package dev.logvinovich.inventario.domain.usecase.sale

import dev.logvinovich.inventario.domain.model.Sale
import dev.logvinovich.inventario.domain.repository.SaleRepository
import jakarta.inject.Inject
import kotlinx.datetime.LocalDate

class GetSalesUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(
        warehouseId: Long?,
        dateFrom: LocalDate?,
        dateTo: LocalDate?
    ): Result<List<Sale>> {
        return saleRepository.getSales(warehouseId, dateFrom, dateTo)
    }
}
