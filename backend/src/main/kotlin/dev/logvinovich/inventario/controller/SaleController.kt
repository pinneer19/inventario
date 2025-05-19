package dev.logvinovich.inventario.controller

import dev.logvinovich.inventario.model.SaleDto
import dev.logvinovich.inventario.model.SaleItemDto
import dev.logvinovich.inventario.model.StatsDto
import dev.logvinovich.inventario.model.toResponseEntity
import dev.logvinovich.inventario.service.sale.SaleService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/sales")
class SaleController(
    private val saleService: SaleService
) {
    @PostMapping
    fun createSale(
        @RequestParam warehouseId: Long,
        @RequestBody items: List<SaleItemDto>
    ): ResponseEntity<SaleDto> {
        val sale = saleService.createSale(warehouseId, items)
        return sale?.let { ResponseEntity.ok(it) } ?: ResponseEntity.badRequest().build()
    }

    @GetMapping
    fun getSales(
        @RequestParam(required = false) warehouseId: Long? = null,
        @RequestParam(required = false) fromDate: LocalDate? = null,
        @RequestParam(required = false) toDate: LocalDate? = null,
    ): ResponseEntity<List<SaleDto>> {
        val sales = saleService.getSales(warehouseId, fromDate, toDate)
        return ResponseEntity.ok(sales)
    }

    @GetMapping("/employee/{id}")
    fun getSalesByEmployee(@PathVariable id: Long): ResponseEntity<List<SaleDto>> {
        return ResponseEntity.ok(saleService.getSalesByManagerId(id))
    }

    @GetMapping("/{id}")
    fun getSale(@PathVariable id: Long): ResponseEntity<SaleDto> {
        return saleService.getSaleById(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteSale(@PathVariable id: Long): ResponseEntity<Unit> {
        val deleteResult = saleService.deleteSale(id)
        return deleteResult.toResponseEntity()
    }

    @PutMapping
    fun updateSale(@PathVariable id: Long, @RequestBody saleItems: List<SaleItemDto>): ResponseEntity<SaleDto> {
        val updateResult = saleService.updateSale(id, saleItems)
        return updateResult.toResponseEntity()
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getStatistics(): ResponseEntity<StatsDto> {
        val stats = saleService.getStatistics()
        return ResponseEntity.ok(stats)
    }
}