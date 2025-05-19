package dev.logvinovich.inventario.domain.usecase.warehouse

import dev.logvinovich.inventario.domain.model.Warehouse
import dev.logvinovich.inventario.domain.repository.WarehouseRepository
import jakarta.inject.Inject

class GetManagerWarehousesUseCase @Inject constructor(
    private val warehouseRepository: WarehouseRepository
) {
    suspend operator fun invoke(managerId: Long): Result<List<Warehouse>> {
        return warehouseRepository.getManagerWarehouses(managerId)
    }
}