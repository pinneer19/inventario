package dev.logvinovich.inventario.domain.repository

import dev.logvinovich.inventario.domain.model.InventoryItem

interface InventoryItemRepository {
    suspend fun getWarehouseItems(warehouseId: Long): Result<List<InventoryItem>>

    suspend fun saveItem(
        inventoryItemId: Long?,
        productId: Long?,
        warehouseId: Long,
        price: Float,
        quantity: Int
    ): Result<InventoryItem>

    suspend fun deleteItem(itemId: Long): Result<Unit>
}