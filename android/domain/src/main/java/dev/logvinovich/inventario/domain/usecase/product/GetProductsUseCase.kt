package dev.logvinovich.inventario.domain.usecase.product

import dev.logvinovich.inventario.domain.model.Product
import dev.logvinovich.inventario.domain.repository.ProductRepository
import jakarta.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(organizationId: Long): Result<List<Product>> {
        return productRepository.getProducts(organizationId)
    }

    suspend fun invokeWithWarehouseId(warehouseId: Long): Result<List<Product>> {
        return productRepository.getOrganizationProductsByWarehouseId(warehouseId)
    }
}