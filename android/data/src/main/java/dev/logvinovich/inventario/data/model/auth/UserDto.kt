package dev.logvinovich.inventario.data.model.auth

import dev.logvinovich.inventario.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val username: String? = null,
    val id: Long? = null
) {
    fun toUser() = User(
        username = username.orEmpty(),
        id = id ?: -1
    )
}