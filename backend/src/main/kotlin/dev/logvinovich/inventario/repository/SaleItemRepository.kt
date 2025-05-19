package dev.logvinovich.inventario.repository

import dev.logvinovich.inventario.entity.SaleItem
import org.springframework.data.jpa.repository.JpaRepository

interface SaleItemRepository : JpaRepository<SaleItem, Long>