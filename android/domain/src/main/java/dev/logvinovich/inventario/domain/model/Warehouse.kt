package dev.logvinovich.inventario.domain.model

data class Warehouse(
    val id: Long,
    val name: String,
    val organizationId: Long,
    val managers: List<User>
)