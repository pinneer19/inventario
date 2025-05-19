package dev.logvinovich.inventario.domain.usecase.warehouse.item

import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.domain.repository.InventoryItemRepository
import jakarta.inject.Inject

class GetWarehouseItemsUseCase @Inject constructor(
    private val inventoryItemRepository: InventoryItemRepository
) {
    suspend operator fun invoke(warehouseId: Long): Result<List<InventoryItem>> {
        return inventoryItemRepository.getWarehouseItems(warehouseId)
    }
}