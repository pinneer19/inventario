package dev.logvinovich.inventario.data.repository

import dev.logvinovich.inventario.data.api.deleteInventoryItem
import dev.logvinovich.inventario.data.api.getWarehouseItems
import dev.logvinovich.inventario.data.api.saveInventoryItem
import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.domain.repository.InventoryItemRepository
import io.ktor.client.HttpClient
import jakarta.inject.Inject

class InventoryItemRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : InventoryItemRepository {
    override suspend fun getWarehouseItems(warehouseId: Long): Result<List<InventoryItem>> {
        return httpClient.getWarehouseItems(warehouseId).map { dtoList ->
            dtoList.map { it.toInventoryItem() }
        }
    }

    override suspend fun saveItem(
        inventoryItemId: Long?,
        productId: Long?,
        warehouseId: Long,
        price: Float,
        quantity: Int
    ): Result<InventoryItem> {
        return httpClient.saveInventoryItem(
            inventoryItemId,
            productId,
            warehouseId,
            price,
            quantity
        ).map {
            it.toInventoryItem()
        }
    }

    override suspend fun deleteItem(itemId: Long): Result<Unit> {
        return httpClient.deleteInventoryItem(itemId)
    }
}