package dev.logvinovich.inventario.service.product

import dev.logvinovich.inventario.entity.Product

interface ProductService {
    fun createProduct(product: Product): Product

    fun getAllProducts(): List<Product>

    fun getProductById(id: Long): Product?

    fun updateProduct(id: Long, product: Product): Product?

    fun deleteProduct(id: Long): Boolean
}