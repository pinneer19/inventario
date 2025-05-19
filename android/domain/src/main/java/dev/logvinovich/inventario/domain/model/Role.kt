package dev.logvinovich.inventario.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    ADMIN, MANAGER
}