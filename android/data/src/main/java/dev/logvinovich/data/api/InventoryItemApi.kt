package dev.logvinovich.data.api

import dev.logvinovich.data.model.inventory.InventoryItemDto
import dev.logvinovich.data.util.apiRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.get

suspend inline fun HttpClient.getWarehouseProducts(
    warehouseId: Long
): Result<List<InventoryItemDto>> {
    return apiRequest {
        get("/inventory/$warehouseId")
    }
}