package dev.logvinovich.domain.usecase

import dev.logvinovich.domain.model.Warehouse
import dev.logvinovich.domain.repository.WarehouseRepository
import jakarta.inject.Inject

class GetOrganizationWarehousesUseCase @Inject constructor(
    private val warehouseRepository: WarehouseRepository
) {
    suspend operator fun invoke(organizationId: Long): Result<List<Warehouse>> {
        return warehouseRepository.getOrganizationWarehouses(organizationId)
    }
}