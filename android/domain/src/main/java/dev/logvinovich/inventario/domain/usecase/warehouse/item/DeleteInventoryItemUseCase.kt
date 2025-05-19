package dev.logvinovich.inventario.domain.usecase.warehouse.item

import dev.logvinovich.inventario.domain.repository.InventoryItemRepository
import jakarta.inject.Inject

class DeleteInventoryItemUseCase @Inject constructor(
    private val inventoryItemRepository: InventoryItemRepository
) {
    suspend operator fun invoke(itemId: Long): Result<Unit> {
        return inventoryItemRepository.deleteItem(itemId)
    }
}