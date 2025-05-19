package dev.logvinovich.inventario.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(val token: String)