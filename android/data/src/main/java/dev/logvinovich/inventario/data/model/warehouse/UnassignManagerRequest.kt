package dev.logvinovich.inventario.data.model.warehouse

import kotlinx.serialization.Serializable

@Serializable
data class UnassignManagerRequest(
    val managerId: Long,
    val warehouseId: Long
)