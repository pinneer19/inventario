package dev.logvinovich.inventario.domain.usecase.warehouse

import dev.logvinovich.inventario.domain.model.Warehouse
import dev.logvinovich.inventario.domain.repository.WarehouseRepository
import jakarta.inject.Inject

class GetOrganizationWarehousesUseCase @Inject constructor(
    private val warehouseRepository: WarehouseRepository
) {
    suspend operator fun invoke(organizationId: Long): Result<List<Warehouse>> {
        return warehouseRepository.getOrganizationWarehouses(organizationId)
    }
}