package dev.logvinovich.data.repository

import dev.logvinovich.data.api.deleteProductById
import dev.logvinovich.data.api.getOrganizationProducts
import dev.logvinovich.data.api.saveProduct
import dev.logvinovich.data.model.inventory.ProductDto
import dev.logvinovich.domain.model.Product
import dev.logvinovich.domain.repository.ProductRepository
import io.ktor.client.HttpClient
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : ProductRepository {
    override suspend fun saveProduct(
        id: Long?,
        name: String,
        description: String?,
        barcode: String,
        organizationId: Long,
        image: ByteArray?
    ): Result<Product> {
        val dto = ProductDto(
            id = id,
            name = name,
            description = description,
            barcode = barcode,
            organizationId = organizationId
        )
        return httpClient.saveProduct(dto, image).map { it.toProduct() }
    }

    override suspend fun getProducts(organizationId: Long): Result<List<Product>> {
        return httpClient.getOrganizationProducts(organizationId).map { dtoList ->
            dtoList.map { it.toProduct() }
        }
    }

    override suspend fun deleteProductById(id: Long): Result<Unit> {
        return httpClient.deleteProductById(id)
    }
}