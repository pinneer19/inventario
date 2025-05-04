package dev.logvinovich.inventario.auth.ui.register

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.logvinovich.domain.model.Role
import kotlinx.serialization.Serializable

@Serializable
private data object RegisterDestination

fun NavGraphBuilder.registerScreen(
    onNavigateToMain: (Role) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    composable<RegisterDestination> {
        RegisterScreen(
            onNavigateToMain = onNavigateToMain,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}

fun NavController.onNavigateToRegister() = navigate(RegisterDestination)