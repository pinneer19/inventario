package dev.logvinovich.inventario.splash.viewmodel

import dev.logvinovich.domain.model.Role

data class TokenUiState(
    val loading: Boolean = false,
    val authenticated: Boolean? = null,
    val userRole: Role? = null
)