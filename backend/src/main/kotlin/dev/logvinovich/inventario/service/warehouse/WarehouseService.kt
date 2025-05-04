package dev.logvinovich.inventario.service.warehouse

import dev.logvinovich.inventario.entity.Warehouse
import dev.logvinovich.inventario.model.WarehouseDto

interface WarehouseService {
    fun createWarehouse(warehouseDto: WarehouseDto): Warehouse?

    fun getWarehousesByOrganizationId(organizationId: Long): List<Warehouse>

    fun findById(warehouseId: Long): Warehouse?

    fun updateWarehouse(warehouse: Warehouse): Warehouse

    fun deleteWarehouseById(warehouseId: Long)
}