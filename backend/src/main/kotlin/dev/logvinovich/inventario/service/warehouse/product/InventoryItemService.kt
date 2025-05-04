package dev.logvinovich.inventario.service.warehouse.product

import dev.logvinovich.inventario.entity.InventoryItem

interface InventoryItemService {
    fun createInventoryItem(item: InventoryItem): InventoryItem

    fun getInventoryItemsByWarehouseId(warehouseId: Long): List<InventoryItem>
}