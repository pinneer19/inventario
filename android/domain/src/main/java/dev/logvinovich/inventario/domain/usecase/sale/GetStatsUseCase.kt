package dev.logvinovich.inventario.domain.usecase.sale

import dev.logvinovich.inventario.domain.model.Stats
import dev.logvinovich.inventario.domain.repository.SaleRepository
import jakarta.inject.Inject

class GetStatsUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(): Result<Stats> {
        return saleRepository.getStats()
    }
}