package dev.logvinovich.inventario.data.model.sale

import dev.logvinovich.inventario.domain.model.SaleItem
import kotlinx.serialization.Serializable

@Serializable
data class SaleItemDto(
    val inventoryItemId: Long,
    val quantity: Int,
    val price: Float,
    val id: Long? = null,
    val productName: String? = null
) {
    fun toSaleItem() = SaleItem(
        id = id,
        price = price,
        quantity = quantity,
        productName = productName.orEmpty(),
        inventoryItemId = inventoryItemId
    )
}

fun SaleItem.toDto() = SaleItemDto(
    id = id,
    price = price,
    quantity = quantity,
    inventoryItemId = inventoryItemId
)