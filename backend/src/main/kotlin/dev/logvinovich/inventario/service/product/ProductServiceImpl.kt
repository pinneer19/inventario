package dev.logvinovich.inventario.service.product

import dev.logvinovich.inventario.entity.Product
import dev.logvinovich.inventario.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductServiceImpl @Autowired constructor(
    private val productRepository: ProductRepository
) : ProductService {

    override fun createProduct(product: Product): Product {
        return productRepository.save(product)
    }

    override fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    override fun getProductById(id: Long): Product? {
        return productRepository.findById(id).orElse(null)
    }

    override fun updateProduct(id: Long, product: Product): Product? {
        return if (productRepository.existsById(id)) {
            val existingProduct = productRepository.findById(id).get()
            val updatedProduct = existingProduct.copy(
                name = product.name,
                description = product.description,
                barcode = product.barcode,
                photo = product.photo
            )
            productRepository.save(updatedProduct)
        } else {
            null
        }
    }

    override fun deleteProduct(id: Long): Boolean {
        return if (productRepository.existsById(id)) {
            productRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}