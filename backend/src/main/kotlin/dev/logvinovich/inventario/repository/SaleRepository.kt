package dev.logvinovich.inventario.repository

import dev.logvinovich.inventario.entity.Sale
import dev.logvinovich.inventario.model.ProductSalesDto
import dev.logvinovich.inventario.model.SalesByDateDto
import dev.logvinovich.inventario.model.WarehouseSalesDto
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SaleRepository : JpaRepository<Sale, Long> {
    @Query(
        """
        SELECT new dev.logvinovich.inventario.model.SalesByDateDto(s.date, SUM(s.totalAmount)) 
        FROM Sale s
        WHERE s.warehouse.id IN :warehouseIds
        GROUP BY s.date 
        ORDER BY s.date
    """
    )
    fun getSalesByDate(@Param("warehouseIds") warehouseIds: List<Long?>): List<SalesByDateDto>


    @Query(
        """
        SELECT new dev.logvinovich.inventario.model.WarehouseSalesDto(s.warehouse.name, SUM(s.totalAmount)) 
        FROM Sale s 
        WHERE s.warehouse.id IN :warehouseIds 
        GROUP BY s.warehouse.name 
        ORDER BY SUM(s.totalAmount) DESC
    """
    )
    fun getTopWarehouses(@Param("warehouseIds") warehouseIds: List<Long?>): List<WarehouseSalesDto>


    @Query(
        """
       SELECT new dev.logvinovich.inventario.model.ProductSalesDto(si.inventoryItem.product.name, SUM(si.quantity * si.price))
       FROM SaleItem si
       WHERE si.sale.warehouse.id IN :warehouseIds
       GROUP BY si.inventoryItem.product.name
       ORDER BY SUM(si.quantity * si.price) DESC 
    """
    )
    fun getTopProducts(@Param("warehouseIds") warehouseIds: List<Long?>, pageable: Pageable): List<ProductSalesDto>
}