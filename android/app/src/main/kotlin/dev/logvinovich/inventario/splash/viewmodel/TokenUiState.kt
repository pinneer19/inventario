package dev.logvinovich.inventario.splash.viewmodel

import dev.logvinovich.inventario.auth.model.UserData

data class TokenUiState(
    val loading: Boolean = false,
    val authenticated: Boolean? = null,
    val userData: UserData? = null
)