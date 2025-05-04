package dev.logvinovich.domain.usecase

import dev.logvinovich.domain.repository.WarehouseRepository
import jakarta.inject.Inject

class DeleteWarehouseUseCase @Inject constructor(
    private val warehouseRepository: WarehouseRepository
) {
    suspend operator fun invoke(warehouseId: Long): Result<Unit> {
        return warehouseRepository.deleteWarehouseById(warehouseId)
    }
}
