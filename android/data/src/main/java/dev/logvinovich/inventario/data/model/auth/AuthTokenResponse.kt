package dev.logvinovich.inventario.data.model.auth

import dev.logvinovich.inventario.domain.model.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenDto(
    val accessToken: String,
    val refreshToken: String
) {
    fun toAuthToken() = AuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}