package dev.logvinovich.inventario.domain.usecase.product

import dev.logvinovich.inventario.domain.model.Product
import dev.logvinovich.inventario.domain.repository.ProductRepository
import jakarta.inject.Inject

class SaveProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(
        id: Long?,
        name: String,
        description: String?,
        barcode: String,
        organizationId: Long,
        image: ByteArray?
    ): Result<Product> {
        return productRepository.saveProduct(id, name, description, barcode, organizationId, image)
    }
}