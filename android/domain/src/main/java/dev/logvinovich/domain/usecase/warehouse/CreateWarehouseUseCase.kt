package dev.logvinovich.domain.usecase.warehouse

import dev.logvinovich.domain.model.Warehouse
import dev.logvinovich.domain.repository.WarehouseRepository
import jakarta.inject.Inject

class CreateWarehouseUseCase @Inject constructor(
    private val warehouseRepository: WarehouseRepository
) {
    suspend operator fun invoke(warehouseName: String, organizationId: Long): Result<Warehouse> {
        return warehouseRepository.createWarehouse(warehouseName, organizationId)
    }
}