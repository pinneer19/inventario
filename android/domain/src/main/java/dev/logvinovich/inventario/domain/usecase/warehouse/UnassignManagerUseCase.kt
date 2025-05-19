package dev.logvinovich.inventario.domain.usecase.warehouse

import dev.logvinovich.inventario.domain.model.User
import dev.logvinovich.inventario.domain.repository.WarehouseRepository
import jakarta.inject.Inject

class UnassignManagerUseCase @Inject constructor(
    private val warehouseRepository: WarehouseRepository
) {
    suspend operator fun invoke(managerId: Long, warehouseId: Long): Result<User> {
        return warehouseRepository.unassignManagerFromWarehouse(managerId, warehouseId)
    }
}
