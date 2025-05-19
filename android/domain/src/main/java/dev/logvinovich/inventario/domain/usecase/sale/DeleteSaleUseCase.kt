package dev.logvinovich.inventario.domain.usecase.sale

import dev.logvinovich.inventario.domain.repository.SaleRepository
import jakarta.inject.Inject

class DeleteSaleUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(saleId: Long): Result<Unit> {
        return saleRepository.deleteSale(saleId)
    }
}
