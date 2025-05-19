package dev.logvinovich.inventario.service.warehouse.product

import dev.logvinovich.inventario.entity.InventoryItem
import dev.logvinovich.inventario.model.ServiceResult
import dev.logvinovich.inventario.model.UpdateInventoryItemDto

interface InventoryItemService {
    fun createInventoryItem(item: UpdateInventoryItemDto): ServiceResult<InventoryItem>

    fun updateInventoryItem(id: Long, item: UpdateInventoryItemDto): ServiceResult<InventoryItem>

    fun getInventoryItemsByWarehouseId(warehouseId: Long): List<InventoryItem>

    fun deleteInventoryItem(itemId: Long): ServiceResult<Unit>
}