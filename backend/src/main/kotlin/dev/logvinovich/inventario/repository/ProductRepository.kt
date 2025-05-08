package dev.logvinovich.inventario.repository

import dev.logvinovich.inventario.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByOrganizationId(organizationId: Long): List<Product>
}