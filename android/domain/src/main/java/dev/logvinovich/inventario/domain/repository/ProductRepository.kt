package dev.logvinovich.inventario.domain.repository

import dev.logvinovich.inventario.domain.model.Product

interface ProductRepository {
    suspend fun saveProduct(
        id: Long?,
        name: String,
        description: String?,
        barcode: String,
        organizationId: Long,
        image: ByteArray?
    ): Result<Product>

    suspend fun getProducts(organizationId: Long): Result<List<Product>>

    suspend fun getOrganizationProductsByWarehouseId(warehouseId: Long): Result<List<Product>>

    suspend fun deleteProductById(id: Long): Result<Unit>
}