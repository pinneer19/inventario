package dev.logvinovich.inventario.data.repository

import dev.logvinovich.inventario.data.api.assignManagerToWarehouse
import dev.logvinovich.inventario.data.api.createWarehouse
import dev.logvinovich.inventario.data.api.deleteWarehouseById
import dev.logvinovich.inventario.data.api.getManagerWarehouses
import dev.logvinovich.inventario.data.api.getOrganizationWarehouses
import dev.logvinovich.inventario.data.api.getWarehouses
import dev.logvinovich.inventario.data.api.unassignManagerFromWarehouse
import dev.logvinovich.inventario.data.model.warehouse.AssignManagerRequest
import dev.logvinovich.inventario.data.model.warehouse.UnassignManagerRequest
import dev.logvinovich.inventario.data.model.warehouse.WarehouseDto
import dev.logvinovich.inventario.domain.model.User
import dev.logvinovich.inventario.domain.model.Warehouse
import dev.logvinovich.inventario.domain.repository.WarehouseRepository
import io.ktor.client.HttpClient
import javax.inject.Inject

class WarehouseRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : WarehouseRepository {
    override suspend fun getOrganizationWarehouses(organizationId: Long): Result<List<Warehouse>> {
        return httpClient.getOrganizationWarehouses(organizationId).map { dtoList ->
            dtoList.map { it.toWarehouse() }
        }
    }

    override suspend fun createWarehouse(
        warehouseName: String,
        organizationId: Long
    ): Result<Warehouse> {
        val warehouse = WarehouseDto(name = warehouseName, organizationId = organizationId)
        return httpClient.createWarehouse(warehouse)
            .map { it.toWarehouse() }
    }

    override suspend fun assignManagerToWarehouse(
        managerUsername: String,
        warehouseId: Long
    ): Result<User> {
        val body = AssignManagerRequest(managerUsername, warehouseId)
        return httpClient.assignManagerToWarehouse(body).map { it.toUser() }
    }

    override suspend fun unassignManagerFromWarehouse(
        managerId: Long,
        warehouseId: Long
    ): Result<User> {
        val body = UnassignManagerRequest(managerId, warehouseId)
        return httpClient.unassignManagerFromWarehouse(body).map { it.toUser() }
    }

    override suspend fun deleteWarehouseById(warehouseId: Long): Result<Unit> {
        return httpClient.deleteWarehouseById(warehouseId)
    }

    override suspend fun getManagerWarehouses(managerId: Long): Result<List<Warehouse>> {
        return httpClient.getManagerWarehouses(managerId).map { dtoList ->
            dtoList.map { it.toWarehouse() }
        }
    }

    override suspend fun getWarehouses(): Result<List<Warehouse>> {
        return httpClient.getWarehouses().map { dtoList ->
            dtoList.map { it.toWarehouse() }
        }
    }
}