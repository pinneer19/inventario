package dev.logvinovich.inventario.data.repository

import dev.logvinovich.inventario.data.api.deleteProductById
import dev.logvinovich.inventario.data.api.getOrganizationProducts
import dev.logvinovich.inventario.data.api.saveProduct
import dev.logvinovich.inventario.data.model.inventory.ProductDto
import dev.logvinovich.inventario.domain.model.Product
import dev.logvinovich.inventario.domain.repository.ProductRepository
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
        return httpClient.getOrganizationProducts(organizationId = organizationId).map { dtoList ->
            dtoList.map { it.toProduct() }
        }
    }

    override suspend fun deleteProductById(id: Long): Result<Unit> {
        return httpClient.deleteProductById(id)
    }

    override suspend fun getOrganizationProductsByWarehouseId(
        warehouseId: Long
    ): Result<List<Product>> {
        return httpClient.getOrganizationProducts(warehouseId = warehouseId).map { dtoList ->
            dtoList.map { it.toProduct() }
        }
    }
}