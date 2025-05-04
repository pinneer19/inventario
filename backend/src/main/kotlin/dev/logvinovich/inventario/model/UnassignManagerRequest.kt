package dev.logvinovich.inventario.model

data class UnassignManagerRequest(
    val managerId: Long,
    val warehouseId: Long
)