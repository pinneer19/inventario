package dev.logvinovich.inventario.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)