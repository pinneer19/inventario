package dev.logvinovich.inventario.domain.usecase.warehouse

import dev.logvinovich.inventario.domain.model.Warehouse
import dev.logvinovich.inventario.domain.repository.WarehouseRepository
import jakarta.inject.Inject

class CreateWarehouseUseCase @Inject constructor(
    private val warehouseRepository: WarehouseRepository
) {
    suspend operator fun invoke(warehouseName: String, organizationId: Long): Result<Warehouse> {
        return warehouseRepository.createWarehouse(warehouseName, organizationId)
    }
}