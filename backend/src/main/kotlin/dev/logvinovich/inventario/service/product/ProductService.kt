package dev.logvinovich.inventario.service.product

import dev.logvinovich.inventario.entity.Product
import dev.logvinovich.inventario.model.ProductDto
import dev.logvinovich.inventario.model.ServiceResult
import org.springframework.web.multipart.MultipartFile

interface ProductService {
    fun createProduct(product: ProductDto, productImage: MultipartFile?): ServiceResult<Product>

    fun getOrganizationProducts(organizationId: Long): List<Product>

    fun getProductById(id: Long): Product?

    fun updateProduct(id: Long, product: ProductDto, productImage: MultipartFile?): ServiceResult<Product>

    fun deleteProduct(id: Long): ServiceResult<Unit>
}