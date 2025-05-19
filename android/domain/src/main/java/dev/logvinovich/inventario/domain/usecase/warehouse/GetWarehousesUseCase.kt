package dev.logvinovich.inventario.domain.usecase.warehouse

import dev.logvinovich.inventario.domain.model.Warehouse
import dev.logvinovich.inventario.domain.repository.WarehouseRepository
import jakarta.inject.Inject

class GetWarehousesUseCase @Inject constructor(
    private val warehouseRepository: WarehouseRepository
) {
    suspend operator fun invoke(): Result<List<Warehouse>> {
        return warehouseRepository.getWarehouses()
    }
}
