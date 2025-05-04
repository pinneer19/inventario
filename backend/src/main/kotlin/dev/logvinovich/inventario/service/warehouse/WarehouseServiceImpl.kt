package dev.logvinovich.inventario.service.warehouse

import dev.logvinovich.inventario.entity.Warehouse
import dev.logvinovich.inventario.model.WarehouseDto
import dev.logvinovich.inventario.repository.WarehouseRepository
import dev.logvinovich.inventario.service.organization.OrganizationService
import org.springframework.stereotype.Service

@Service
class WarehouseServiceImpl(
    private val warehouseRepository: WarehouseRepository,
    private val organizationService: OrganizationService
) : WarehouseService {
    override fun createWarehouse(warehouseDto: WarehouseDto): Warehouse? {
        val organization = organizationService.getOrganizationById(warehouseDto.organizationId) ?: return null
        val warehouse = Warehouse(name = warehouseDto.name, organization = organization)
        return warehouseRepository.save(warehouse)
    }

    override fun getWarehousesByOrganizationId(organizationId: Long): List<Warehouse> {
        return warehouseRepository.getWarehousesByOrganizationId(organizationId)
    }

    override fun findById(warehouseId: Long): Warehouse? {
        return warehouseRepository.findById(warehouseId).orElse(null)
    }

    override fun updateWarehouse(warehouse: Warehouse): Warehouse {
        return warehouseRepository.save(warehouse)
    }

    override fun deleteWarehouseById(warehouseId: Long) {
        return warehouseRepository.deleteById(warehouseId)
    }
}