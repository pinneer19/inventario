package dev.logvinovich.inventario.service.warehouse

import dev.logvinovich.inventario.entity.User
import dev.logvinovich.inventario.entity.Warehouse
import dev.logvinovich.inventario.model.AssignManagerRequest
import dev.logvinovich.inventario.model.ServiceResult
import dev.logvinovich.inventario.model.UnassignManagerRequest
import dev.logvinovich.inventario.model.WarehouseDto

interface WarehouseService {
    fun createWarehouse(warehouseDto: WarehouseDto): Warehouse?

    fun getWarehousesByOrganizationId(organizationId: Long): List<Warehouse>

    fun findById(warehouseId: Long): Warehouse?

    fun updateWarehouse(warehouseDto: WarehouseDto): ServiceResult<Warehouse>

    fun deleteWarehouseById(warehouseId: Long): ServiceResult<Unit>

    fun assignManagerToWarehouse(request: AssignManagerRequest): ServiceResult<User>

    fun unassignManagerFromWarehouse(request: UnassignManagerRequest): ServiceResult<User>
}