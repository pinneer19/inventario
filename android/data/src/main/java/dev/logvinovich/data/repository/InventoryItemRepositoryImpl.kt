package dev.logvinovich.data.repository

import dev.logvinovich.data.api.getWarehouseProducts
import dev.logvinovich.domain.model.InventoryItem
import dev.logvinovich.domain.repository.InventoryItemRepository
import io.ktor.client.HttpClient
import jakarta.inject.Inject

class InventoryItemRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : InventoryItemRepository {
    override suspend fun getWarehouseProducts(warehouseId: Long): Result<List<InventoryItem>> {
        return httpClient.getWarehouseProducts(warehouseId).map { dtoList ->
            dtoList.map { it.toInventoryItem() }
        }
    }
}