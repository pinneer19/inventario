package dev.logvinovich.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)