package dev.logvinovich.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(val token: String)