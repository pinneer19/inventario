package dev.logvinovich.inventario.data.model.warehouse

import kotlinx.serialization.Serializable

@Serializable
data class AssignManagerRequest(
    val managerUsername: String,
    val warehouseId: Long
)