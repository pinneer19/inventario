package dev.logvinovich.inventario.repository

import dev.logvinovich.inventario.entity.InventoryItem
import org.springframework.data.jpa.repository.JpaRepository

interface InventoryItemRepository: JpaRepository<InventoryItem, Long> {
    fun findAllByWarehouseId(warehouseId: Long): List<InventoryItem>
    fun findOneByWarehouseIdAndProductId(warehouseId: Long, productId: Long): InventoryItem?
}