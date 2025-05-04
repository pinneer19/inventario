package dev.logvinovich.inventario.model

import dev.logvinovich.inventario.entity.Organization

data class OrganizationDto(
    val id: Long? = null,
    val name: String,
    val adminUserId: Long
)

fun Organization.toDto() = OrganizationDto(
    id = id,
    name = name,
    adminUserId = adminUser.id ?: -1
)