package dev.logvinovich.domain.usecase.warehouse

import dev.logvinovich.domain.model.InventoryItem
import dev.logvinovich.domain.repository.InventoryItemRepository
import jakarta.inject.Inject

class GetWarehouseProductsUseCase @Inject constructor(
    private val inventoryItemRepository: InventoryItemRepository
) {
    suspend operator fun invoke(warehouseId: Long): Result<List<InventoryItem>> {
        return inventoryItemRepository.getWarehouseProducts(warehouseId)
    }
}