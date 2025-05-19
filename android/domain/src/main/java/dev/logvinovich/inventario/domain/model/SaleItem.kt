package dev.logvinovich.inventario.domain.model

data class SaleItem(
    val id: Long?,
    val inventoryItemId: Long,
    val productName: String,
    val quantity: Int,
    val price: Float
)