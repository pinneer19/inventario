package dev.logvinovich.inventario.auth.model

import dev.logvinovich.inventario.domain.model.Role
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class UserData(
    val id: Long = -1,
    val role: Role = Role.MANAGER
) {
    fun toJson() = Json.encodeToString(this)
}