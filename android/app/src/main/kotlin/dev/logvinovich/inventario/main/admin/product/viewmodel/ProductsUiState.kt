package dev.logvinovich.inventario.main.admin.product.viewmodel

import dev.logvinovich.inventario.domain.model.Product
import dev.logvinovich.inventario.ui.util.UiText

data class ProductsUiState(
    val loading: Boolean = false,
    val uiMessage: UiText? = null,
    val organizationId: Long? = null,
    val products: List<Product> = emptyList()
)