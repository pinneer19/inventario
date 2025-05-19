package dev.logvinovich.inventario.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InventoryItem(
    val id: Long,
    val product: Product,
    val warehouseId: Long,
    val price: Float,
    val quantity: Int
)