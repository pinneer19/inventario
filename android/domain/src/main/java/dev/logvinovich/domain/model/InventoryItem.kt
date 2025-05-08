package dev.logvinovich.domain.model

data class InventoryItem(
    val id: Long,
    val product: Product,
    val warehouseId: Long,
    val price: Float,
    val quantity: Int
)