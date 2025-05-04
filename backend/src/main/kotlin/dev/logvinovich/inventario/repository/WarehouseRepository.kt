package dev.logvinovich.inventario.repository

import dev.logvinovich.inventario.entity.Warehouse
import org.springframework.data.jpa.repository.JpaRepository

interface WarehouseRepository: JpaRepository<Warehouse, Long> {
    fun getWarehousesByOrganizationId(organizationId: Long): List<Warehouse>
}