package dev.logvinovich.inventario.main.admin.product.viewmodel

import dev.logvinovich.inventario.domain.model.Product

interface ProductsIntent {
    data class SetOrganizationId(val organizationId: Long) : ProductsIntent

    data class DeleteProduct(val productId: Long) : ProductsIntent

    data class SetSavedProduct(val product: Product) : ProductsIntent
}