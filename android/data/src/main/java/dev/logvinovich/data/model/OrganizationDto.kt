package dev.logvinovich.data.model

import dev.logvinovich.domain.model.Organization
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

internal fun Organization.toDto() = OrganizationDto(
    id = id,
    name = name,
    adminUserId = adminUserId
)