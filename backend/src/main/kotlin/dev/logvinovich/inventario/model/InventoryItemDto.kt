package dev.logvinovich.inventario.model

import dev.logvinovich.inventario.entity.InventoryItem
import dev.logvinovich.inventario.entity.Product
import dev.logvinovich.inventario.entity.Warehouse

data class InventoryItemDto(
    val id: Long? = null,
    val warehouseId: Long? = null,
    val product: ProductDto,
    val quantity: Int,
    val price: Float
)

fun InventoryItem.toDto() = InventoryItemDto(
    id = id,
    warehouseId = warehouse.id,
    product = product.toDto(),
    quantity = quantity,
    price = price,
)

data class UpdateInventoryItemDto(
    val warehouseId: Long,
    val productId: Long,
    val quantity: Int,
    val price: Float
) {
    fun toInventoryItem(warehouse: Warehouse, product: Product) = InventoryItem(
        warehouse = warehouse,
        product = product,
        quantity = quantity,
        price = price,
    )
}