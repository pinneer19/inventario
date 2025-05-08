package dev.logvinovich.domain.repository

import dev.logvinovich.domain.model.InventoryItem

interface InventoryItemRepository {
    suspend fun getWarehouseProducts(warehouseId: Long): Result<List<InventoryItem>>
}