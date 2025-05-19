package dev.logvinovich.inventario.domain.usecase.warehouse

import dev.logvinovich.inventario.domain.model.User
import dev.logvinovich.inventario.domain.repository.WarehouseRepository
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