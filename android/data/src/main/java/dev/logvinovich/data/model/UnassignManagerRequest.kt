package dev.logvinovich.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UnassignManagerRequest(
    val managerId: Long,
    val warehouseId: Long
)