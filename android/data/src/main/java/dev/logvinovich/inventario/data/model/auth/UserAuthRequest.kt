package dev.logvinovich.inventario.data.model.auth

import dev.logvinovich.inventario.domain.model.Role
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthRequest(
    val username: String,
    val password: String,
    val role: Role = Role.MANAGER
)