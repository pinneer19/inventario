package dev.logvinovich.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(val token: String)