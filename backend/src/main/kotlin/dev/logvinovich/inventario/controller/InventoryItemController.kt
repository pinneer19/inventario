package dev.logvinovich.inventario.controller

import dev.logvinovich.inventario.entity.InventoryItem
import dev.logvinovich.inventario.model.InventoryItemDto
import dev.logvinovich.inventario.model.UpdateInventoryItemDto
import dev.logvinovich.inventario.model.toDto
import dev.logvinovich.inventario.model.toResponseEntity
import dev.logvinovich.inventario.service.warehouse.product.InventoryItemService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    fun createInventoryItem(@RequestBody inventoryItem: UpdateInventoryItemDto): ResponseEntity<InventoryItemDto> {
        val createResult = inventoryItemService.createInventoryItem(inventoryItem)
        return createResult.toResponseEntity(InventoryItem::toDto)
    }

    @PutMapping("/{id}")
    fun updateInventoryItem(
        @PathVariable id: Long,
        @RequestBody inventoryItem: UpdateInventoryItemDto
    ): ResponseEntity<InventoryItemDto> {
        val createResult = inventoryItemService.updateInventoryItem(id, inventoryItem)
        return createResult.toResponseEntity(InventoryItem::toDto)
    }

    @GetMapping
    fun getWarehouseInventoryItems(@RequestParam warehouseId: Long): ResponseEntity<List<InventoryItemDto>> {
        return ResponseEntity.ok(
            inventoryItemService.getInventoryItemsByWarehouseId(warehouseId).map { it.toDto() }
        )
    }

    @DeleteMapping("/{id}")
    fun deleteInventoryItem(@PathVariable id: Long): ResponseEntity<Unit> {
        val deleteResult = inventoryItemService.deleteInventoryItem(id)
        return deleteResult.toResponseEntity()
    }
}