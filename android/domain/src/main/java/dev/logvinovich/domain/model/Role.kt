package dev.logvinovich.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    ADMIN, MANAGER
}