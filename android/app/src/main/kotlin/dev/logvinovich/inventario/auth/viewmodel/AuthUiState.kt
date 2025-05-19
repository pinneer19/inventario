package dev.logvinovich.inventario.auth.viewmodel

import dev.logvinovich.inventario.auth.model.UserData

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val username: String = "",
    val password: String = "",
    val usernameError: Boolean = false,
    val passwordError: Boolean = false,
    val passwordVisible: Boolean = false,
    val authenticated: Boolean = false,
    val userData: UserData = UserData()
)