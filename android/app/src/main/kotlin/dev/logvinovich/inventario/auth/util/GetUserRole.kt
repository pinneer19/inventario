package dev.logvinovich.inventario.auth.util

import com.auth0.android.jwt.JWT
import dev.logvinovich.domain.model.Role

fun getUserRole(token: String): Role {
    return Role.valueOf(
        JWT(token).claims["role"]
            ?.asString()
            .orEmpty()
    )
}