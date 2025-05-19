package dev.logvinovich.inventario.model

import dev.logvinovich.inventario.entity.User
import dev.logvinovich.inventario.entity.Warehouse

data class WarehouseDto(
    val id: Long? = null,
    val name: String,
    val organizationId: Long,
    val managers: List<User> = emptyList()
)

fun Warehouse.toDto() = WarehouseDto(
    id = id,
    name = name,
    organizationId = organization.id ?: -1,
    managers = managers.toList()
)