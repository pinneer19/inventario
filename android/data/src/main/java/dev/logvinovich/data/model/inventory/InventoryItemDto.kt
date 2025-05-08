package dev.logvinovich.data.model.inventory

import dev.logvinovich.domain.model.InventoryItem
import kotlinx.serialization.Serializable

@Serializable
data class InventoryItemDto(
    val id: Long? = null,
    val product: ProductDto,
    val warehouseId: Long? = null,
    val price: Float? = null,
    val quantity: Int? = null
) {
    fun toInventoryItem() = InventoryItem(
        id = id ?: -1,
        product = product.toProduct(),
        warehouseId = warehouseId ?: -1,
        price = price ?: 0f,
        quantity = quantity ?: 0,
    )
}