package dev.logvinovich.inventario.controller

import dev.logvinovich.inventario.entity.Product
import dev.logvinovich.inventario.model.ProductDto
import dev.logvinovich.inventario.model.toDto
import dev.logvinovich.inventario.model.toResponseEntity
import dev.logvinovich.inventario.service.product.ProductService
import dev.logvinovich.inventario.service.warehouse.WarehouseService
import dev.logvinovich.inventario.util.ImageUtil
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
    private val warehouseService: WarehouseService,
    private val imageUtil: ImageUtil
) {
    @PostMapping
    fun createProduct(
        @RequestPart product: ProductDto,
        @RequestPart(required = false) productImage: MultipartFile?
    ): ResponseEntity<ProductDto> {
        val productResult = productService.createProduct(product, productImage)
        return productResult.toResponseEntity(Product::toDto)
    }

    @GetMapping
    fun getOrganizationProducts(
        @RequestParam(required = false) organizationId: Long?,
        @RequestParam(required = false) warehouseId: Long?
    ): ResponseEntity<List<ProductDto>> {
        return when {
            organizationId != null -> {
                val products = productService.getOrganizationProducts(organizationId).map { it.toDto() }
                ResponseEntity.ok(products)
            }

            warehouseId != null -> {
                val warehouse = warehouseService.findById(warehouseId)
                    ?: return ResponseEntity.badRequest().build()
                val products =
                    productService.getOrganizationProducts(
                        requireNotNull(warehouse.organization.id)
                    ).map { it.toDto() }
                ResponseEntity.ok(products)
            }

            else -> ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> {
        val product = productService.getProductById(id)
        return product?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestPart product: ProductDto,
        @RequestPart(required = false) productImage: MultipartFile?
    ): ResponseEntity<ProductDto> {
        val productResult = productService.updateProduct(id, product, productImage)
        return productResult.toResponseEntity(Product::toDto)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Unit> {
        val deleteResult = productService.deleteProduct(id)
        return deleteResult.toResponseEntity()
    }

    @GetMapping("/image/{filename}")
    fun getProductImage(@PathVariable filename: String): ResponseEntity<ByteArray> {
        val imageBytes = imageUtil.getImage(filename)

        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(imageBytes)
    }
}
