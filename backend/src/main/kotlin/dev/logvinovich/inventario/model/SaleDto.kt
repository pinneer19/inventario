package dev.logvinovich.inventario.model

import dev.logvinovich.inventario.entity.Sale
import java.time.LocalDateTime

data class SaleDto(
    val id: Long?,
    val date: LocalDateTime?,
    val totalAmount: Float?,
    val managerId: Long,
    val warehouseId: Long,
    val items: List<SaleItemDto>
)

fun Sale.toDto() = SaleDto(
    id = id,
    managerId = requireNotNull(manager.id),
    warehouseId = requireNotNull(warehouse.id),
    date = date,
    totalAmount = totalAmount,
    items = items.map { it.toDto() }
)