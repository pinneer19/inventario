package dev.logvinovich.inventario.data.model.inventory

import dev.logvinovich.inventario.domain.model.Product
import kotlinx.serialization.Serializable
import kotlin.Long

@Serializable
data class ProductDto(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val barcode: String? = null,
    val imageUrl: String? = null,
    val organizationId: Long? = null
) {
    fun toProduct() = Product(
        id = id ?: -1,
        name = name.orEmpty(),
        description = description.orEmpty(),
        barcode = barcode.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        organizationId = organizationId ?: -1
    )
}