package dev.logvinovich.inventario.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val barcode: String,
    val imageUrl: String,
    val organizationId: Long
)