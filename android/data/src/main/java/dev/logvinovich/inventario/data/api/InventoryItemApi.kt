package dev.logvinovich.inventario.data.api

import dev.logvinovich.inventario.data.model.inventory.InventoryItemDto
import dev.logvinovich.inventario.data.model.inventory.SaveInventoryItemDto
import dev.logvinovich.inventario.data.util.apiRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

suspend inline fun HttpClient.getWarehouseItems(
    warehouseId: Long
): Result<List<InventoryItemDto>> {
    return apiRequest {
        get("/inventory") {
            parameter("warehouseId", warehouseId)
        }
    }
}

suspend inline fun HttpClient.saveInventoryItem(
    inventoryItemId: Long?,
    productId: Long?,
    warehouseId: Long,
    price: Float,
    quantity: Int
): Result<InventoryItemDto> {
    val dto = SaveInventoryItemDto(
        productId = productId,
        warehouseId = warehouseId,
        price = price,
        quantity = quantity
    )
    return apiRequest {
        inventoryItemId?.let {
            put("/inventory/$it") {
                setBody(dto)
            }
        } ?: post("/inventory") {
            setBody(dto)
        }
    }
}

suspend inline fun HttpClient.deleteInventoryItem(itemId: Long): Result<Unit> {
    return apiRequest {
        delete("/inventory/$itemId")
    }
}