package dev.logvinovich.inventario.auth.viewmodel

import androidx.credentials.GetCredentialResponse

sealed interface AuthIntent {
    data class GoogleAuth(val credentialResponse: GetCredentialResponse) : AuthIntent

    data object Login : AuthIntent

    data object Register : AuthIntent

    data class UpdateUsername(val username: String) : AuthIntent

    data class UpdatePassword(val password: String) : AuthIntent

    data object ClearUsername : AuthIntent

    data object TogglePasswordVisibility : AuthIntent

    data object ToggleUserRole : AuthIntent
}