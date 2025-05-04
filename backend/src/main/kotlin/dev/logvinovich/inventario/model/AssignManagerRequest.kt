package dev.logvinovich.inventario.model

data class AssignManagerRequest(
    val managerUsername: String,
    val warehouseId: Long
)