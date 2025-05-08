package dev.logvinovich.domain.usecase.product

import dev.logvinovich.domain.repository.ProductRepository
import jakarta.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Long): Result<Unit> {
        return productRepository.deleteProductById(productId)
    }
}