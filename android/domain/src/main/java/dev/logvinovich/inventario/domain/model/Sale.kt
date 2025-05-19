package dev.logvinovich.inventario.domain.model

import kotlinx.datetime.LocalDateTime

data class Sale(
    val id: Long,
    val managerId: Long,
    val date: LocalDateTime,
    val totalAmount: Float,
    val items: List<SaleItem>
)