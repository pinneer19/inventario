package dev.logvinovich.domain.usecase.warehouse

import dev.logvinovich.domain.model.User
import dev.logvinovich.domain.repository.WarehouseRepository
import jakarta.inject.Inject

class AssignManagerUseCase @Inject constructor(
    private val warehouseRepository: WarehouseRepository
) {
    suspend operator fun invoke(
        managerUsername: String,
        warehouseId: Long
    ): Result<User> {
        return warehouseRepository.assignManagerToWarehouse(managerUsername, warehouseId)
    }
}