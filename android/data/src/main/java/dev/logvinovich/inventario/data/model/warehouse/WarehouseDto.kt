package dev.logvinovich.inventario.data.model.warehouse

import dev.logvinovich.inventario.data.model.auth.UserDto
import dev.logvinovich.inventario.domain.model.Warehouse
import kotlinx.serialization.Serializable

@Serializable
data class WarehouseDto(
    val id: Long? = null,
    val name: String? = null,
    val organizationId: Long? = null,
    val managers: List<UserDto> = emptyList()
) {
    fun toWarehouse() = Warehouse(
        id = id ?: -1,
        name = name.orEmpty(),
        organizationId = organizationId ?: -1,
        managers = managers.map { it.toUser() }
    )
}