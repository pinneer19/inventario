package dev.logvinovich.inventario.service.product

import dev.logvinovich.inventario.entity.Product
import dev.logvinovich.inventario.model.ProductDto
import dev.logvinovich.inventario.model.ServiceResult
import dev.logvinovich.inventario.repository.OrganizationRepository
import dev.logvinovich.inventario.repository.ProductRepository
import dev.logvinovich.inventario.util.ImageUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val organizationRepository: OrganizationRepository,
    private val imageUtil: ImageUtil
) : ProductService {
    override fun createProduct(product: ProductDto, productImage: MultipartFile?): ServiceResult<Product> {
        val organization = organizationRepository.findById(product.organizationId).orElse(null)
            ?: return ServiceResult.NotFound

        val imageUrl = productImage?.let { image -> imageUtil.saveImage(image) }
        val result = productRepository.save(
            Product(
                name = product.name,
                description = product.description,
                barcode = product.barcode,
                imageUrl = imageUrl,
                organization = organization
            )
        )
        return ServiceResult.Success(result)
    }

    override fun getOrganizationProducts(organizationId: Long): List<Product> {
        return productRepository.findAllByOrganizationId(organizationId)
    }

    override fun getProductById(id: Long): Product? {
        return productRepository.findById(id).orElse(null)
    }

    override fun updateProduct(id: Long, product: ProductDto, productImage: MultipartFile?): ServiceResult<Product> {
        return if (productRepository.existsById(id)) {
            val existingProduct = productRepository.findById(id).get()

            val imageUrl = productImage?.let { image -> imageUtil.saveImage(image) }
            val updatedProduct = existingProduct.copy(
                name = product.name,
                description = product.description,
                barcode = product.barcode,
                imageUrl = imageUrl ?: product.imageUrl
            )
            productRepository.save(updatedProduct)

            ServiceResult.Success(updatedProduct)
        } else {
            ServiceResult.NotFound
        }
    }

    override fun deleteProduct(id: Long): ServiceResult<Unit> {
        return if (productRepository.existsById(id)) {
            productRepository.deleteById(id)
            ServiceResult.Success(Unit)
        } else {
            ServiceResult.NotFound
        }
    }
}