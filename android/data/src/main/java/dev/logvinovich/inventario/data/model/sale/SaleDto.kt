package dev.logvinovich.inventario.data.model.sale

import dev.logvinovich.inventario.domain.model.Sale
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.collections.map

@Serializable
data class SaleDto(
    val managerId: Long,
    val items: List<SaleItemDto>,
    val id: Long? = null,
    val date: LocalDateTime? = null,
    val totalAmount: Float? = null
) {
    fun toSale() = Sale(
        id = id ?: -1,
        managerId = managerId,
        totalAmount = totalAmount ?: 0f,
        date = date ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        items = items.map { it.toSaleItem() }
    )
}