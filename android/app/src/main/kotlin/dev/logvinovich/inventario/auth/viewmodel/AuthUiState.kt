package dev.logvinovich.inventario.auth.viewmodel

import dev.logvinovich.domain.model.Role

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val username: String = "",
    val password: String = "",
    val usernameError: Boolean = false,
    val passwordError: Boolean = false,
    val passwordVisible: Boolean = false,
    val authenticated: Boolean = false,
    val selectedRole: Role = Role.MANAGER
)