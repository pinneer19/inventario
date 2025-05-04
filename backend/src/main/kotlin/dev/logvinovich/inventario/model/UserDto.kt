package dev.logvinovich.inventario.model

import dev.logvinovich.inventario.entity.User

data class UserDto(
    val id: Long,
    val username: String
)

fun User.toDto() = UserDto(
    id = id ?: -1,
    username = username
)