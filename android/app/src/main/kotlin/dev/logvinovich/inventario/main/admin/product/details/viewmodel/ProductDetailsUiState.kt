package dev.logvinovich.inventario.main.admin.product.details.viewmodel

import dev.logvinovich.inventario.domain.model.Product

data class ProductDetailsUiState(
    val loading: Boolean = false,
    val isEdit: Boolean = false,
    val productId: Long? = null,
    val image: Any? = null,
    val name: String = "",
    val nameError: Boolean = false,
    val description: String = "",
    val barcode: String = "",
    val barcodeError: Boolean = false,
    val saveResult: Product? = null
)

internal fun Product.toDetailsUiState() = ProductDetailsUiState(
    productId = id,
    isEdit = true,
    image = imageUrl,
    name = name,
    description = description,
    barcode = barcode
)