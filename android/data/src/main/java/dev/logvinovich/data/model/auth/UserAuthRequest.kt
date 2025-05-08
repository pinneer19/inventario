package dev.logvinovich.data.model.auth

import dev.logvinovich.domain.model.Role
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthRequest(
    val username: String,
    val password: String,
    val role: Role = Role.MANAGER
)