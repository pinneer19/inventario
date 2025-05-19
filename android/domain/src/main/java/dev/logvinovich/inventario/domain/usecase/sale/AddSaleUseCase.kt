package dev.logvinovich.inventario.domain.usecase.sale

import dev.logvinovich.inventario.domain.model.Sale
import dev.logvinovich.inventario.domain.model.SaleItem
import dev.logvinovich.inventario.domain.repository.SaleRepository
import jakarta.inject.Inject

class AddSaleUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(warehouseId: Long, items: List<SaleItem>): Result<Sale> {
        return saleRepository.addSale(warehouseId, items)
    }
}