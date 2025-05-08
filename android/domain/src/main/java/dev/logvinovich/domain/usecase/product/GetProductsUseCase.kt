package dev.logvinovich.domain.usecase.product

import dev.logvinovich.domain.model.Product
import dev.logvinovich.domain.repository.ProductRepository
import jakarta.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(organizationId: Long): Result<List<Product>> {
        return productRepository.getProducts(organizationId)
    }
}