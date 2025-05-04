package dev.logvinovich.inventario.controller

import dev.logvinovich.inventario.entity.InventoryItem
import dev.logvinovich.inventario.service.warehouse.product.InventoryItemService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/inventory")
class InventoryItemController(
    private val inventoryItemService: InventoryItemService
) {
    @PostMapping
    fun createInventoryItem(@RequestBody inventoryItem: InventoryItem): InventoryItem {
        return inventoryItemService.createInventoryItem(inventoryItem)
    }

    @GetMapping
    fun getWarehouseInventoryItems(@RequestParam warehouseId: Long): List<InventoryItem> {
        return inventoryItemService.getInventoryItemsByWarehouseId(warehouseId)
    }
}