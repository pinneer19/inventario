package dev.logvinovich.inventario.domain.usecase.warehouse.item

import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.domain.repository.InventoryItemRepository
import jakarta.inject.Inject

class SaveInventoryItemUseCase @Inject constructor(
    private val inventoryItemRepository: InventoryItemRepository
) {
    suspend operator fun invoke(
        inventoryItemId: Long?,
        productId: Long?,
        warehouseId: Long,
        price: Float,
        quantity: Int
    ): Result<InventoryItem> {
        return inventoryItemRepository.saveItem(inventoryItemId, productId, warehouseId, price, quantity)
    }
}