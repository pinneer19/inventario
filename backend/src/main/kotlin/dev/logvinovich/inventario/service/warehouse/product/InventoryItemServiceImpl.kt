package dev.logvinovich.inventario.service.warehouse.product

import dev.logvinovich.inventario.entity.InventoryItem
import dev.logvinovich.inventario.repository.InventoryItemRepository
import org.springframework.stereotype.Service

@Service
class InventoryItemServiceImpl(
    private val inventoryItemRepository: InventoryItemRepository
): InventoryItemService {
    override fun createInventoryItem(item: InventoryItem): InventoryItem {
        return inventoryItemRepository.save(item)
    }

    override fun getInventoryItemsByWarehouseId(warehouseId: Long): List<InventoryItem> {
        return inventoryItemRepository.findAllByWarehouseId(warehouseId)
    }
}