package dev.logvinovich.inventario.auth.ui.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

fun NavGraphBuilder.loginScreen(
    onNavigateToMain: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    composable<LoginDestination> {
        LoginScreen(
            onNavigateToMain = onNavigateToMain,
            onNavigateToRegister = onNavigateToRegister,
        )
    }
}

fun NavController.onNavigateToLogin() = navigate(LoginDestination) {
    popUpTo(LoginDestination) {
        inclusive = true
    }
}