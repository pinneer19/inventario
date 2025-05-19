package dev.logvinovich.inventario.auth.util

import com.auth0.android.jwt.JWT
import dev.logvinovich.inventario.domain.model.Role
import dev.logvinovich.inventario.auth.model.UserData

fun getUserData(token: String): UserData {
    val claims = JWT(token).claims
    val role = Role.valueOf(claims["role"]?.asString().orEmpty())
    val userId = claims["userId"]?.asLong()
        ?: throw IllegalArgumentException("User ID is missing or invalid")

    return UserData(userId, role)
}