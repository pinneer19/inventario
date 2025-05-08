package dev.logvinovich.domain.usecase.warehouse

import dev.logvinovich.domain.model.User
import dev.logvinovich.domain.repository.WarehouseRepository
import jakarta.inject.Inject

class UnassignManagerUseCase @Inject constructor(
    private val warehouseRepository: WarehouseRepository
) {
    suspend operator fun invoke(managerId: Long, warehouseId: Long): Result<User> {
        return warehouseRepository.unassignManagerFromWarehouse(managerId, warehouseId)
    }
}
