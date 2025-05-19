package dev.logvinovich.inventario.model

import dev.logvinovich.inventario.entity.Product

data class ProductDto(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val barcode: String,
    val imageUrl: String? = null,
    val organizationId: Long
)

fun Product.toDto() = ProductDto(
    id = id,
    name = name,
    description = description,
    barcode = barcode,
    imageUrl = imageUrl,
    organizationId = organization.id ?: -1
)

data class ProductRefDto(val id: Long)