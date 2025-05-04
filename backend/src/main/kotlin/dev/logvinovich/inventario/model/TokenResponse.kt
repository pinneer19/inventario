package dev.logvinovich.inventario.model

data class TokenResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val error: String? = null
)