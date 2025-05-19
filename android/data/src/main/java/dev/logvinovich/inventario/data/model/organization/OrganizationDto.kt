package dev.logvinovich.inventario.data.model.organization

import dev.logvinovich.inventario.domain.model.Organization
import kotlinx.serialization.Serializable

@Serializable
data class OrganizationDto(
    val id: Long? = null,
    val name: String? = null,
    val adminUserId: Long? = null
) {
    fun toOrganization() = Organization(
        id = id ?: -1,
        name = name.orEmpty(),
        adminUserId = adminUserId ?: -1
    )
}