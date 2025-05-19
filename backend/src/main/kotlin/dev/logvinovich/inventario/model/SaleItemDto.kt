package dev.logvinovich.inventario.model

import dev.logvinovich.inventario.entity.SaleItem

data class SaleItemDto(
    val id: Long?,
    val productName: String?,
    val inventoryItemId: Long,
    val quantity: Int,
    val price: Float
)

fun SaleItem.toDto() = SaleItemDto(
    id = id,
    inventoryItemId = requireNotNull(inventoryItem.id),
    productName = inventoryItem.product.name,
    quantity = quantity,
    price = price
)