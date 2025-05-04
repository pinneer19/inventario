package dev.logvinovich.data.api

import dev.logvinovich.data.model.AssignManagerRequest
import dev.logvinovich.data.model.UnassignManagerRequest
import dev.logvinovich.data.model.UserDto
import dev.logvinovich.data.model.WarehouseDto
import dev.logvinovich.data.util.apiRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

suspend inline fun HttpClient.getOrganizationWarehouses(organizationId: Long): Result<List<WarehouseDto>> {
    return apiRequest {
        get("/warehouses") {
            parameter("organizationId", organizationId)
        }
    }
}

suspend inline fun HttpClient.createWarehouse(warehouse: WarehouseDto): Result<WarehouseDto> {
    return apiRequest {
        post("/warehouses") {
            setBody(warehouse)
        }
    }
}

suspend inline fun HttpClient.assignManagerToWarehouse(body: AssignManagerRequest): Result<UserDto> {
    return apiRequest {
        post("/warehouses/assign-manager") {
            setBody(body)
        }
    }
}

suspend inline fun HttpClient.unassignManagerFromWarehouse(
    body: UnassignManagerRequest
): Result<UserDto> {
    return apiRequest {
        post("/warehouses/unassign-manager") {
            setBody(body)
        }
    }
}

suspend inline fun HttpClient.deleteWarehouseById(warehouseId: Long): Result<Unit> {
    return apiRequest {
        delete("/warehouses") {
            parameter("warehouseId", warehouseId)
        }
    }
}