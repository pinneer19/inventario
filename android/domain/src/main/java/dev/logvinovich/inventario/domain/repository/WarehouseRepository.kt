package dev.logvinovich.inventario.domain.repository

import dev.logvinovich.inventario.domain.model.User
import dev.logvinovich.inventario.domain.model.Warehouse

interface WarehouseRepository {
    suspend fun getOrganizationWarehouses(organizationId: Long): Result<List<Warehouse>>

    suspend fun createWarehouse(warehouseName: String, organizationId: Long): Result<Warehouse>

    suspend fun assignManagerToWarehouse(managerUsername: String, warehouseId: Long): Result<User>

    suspend fun unassignManagerFromWarehouse(managerId: Long, warehouseId: Long): Result<User>

    suspend fun deleteWarehouseById(warehouseId: Long): Result<Unit>

    suspend fun getManagerWarehouses(managerId: Long): Result<List<Warehouse>>

    suspend fun getWarehouses(): Result<List<Warehouse>>
}