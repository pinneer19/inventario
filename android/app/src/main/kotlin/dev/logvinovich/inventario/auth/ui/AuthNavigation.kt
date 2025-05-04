package dev.logvinovich.inventario.auth.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import dev.logvinovich.inventario.auth.ui.login.LoginDestination
import dev.logvinovich.inventario.auth.ui.login.loginScreen
import dev.logvinovich.inventario.auth.ui.login.onNavigateToLogin
import dev.logvinovich.inventario.auth.ui.register.onNavigateToRegister
import dev.logvinovich.inventario.auth.ui.register.registerScreen
import dev.logvinovich.inventario.main.ui.onNavigateToMainGraph
import kotlinx.serialization.Serializable

@Serializable
data object AuthGraph

fun NavGraphBuilder.authNavigation(navController: NavController) {
    navigation<AuthGraph>(startDestination = LoginDestination) {
        loginScreen(
            onNavigateToMain = navController::onNavigateToMainGraph,
            onNavigateToRegister = navController::onNavigateToRegister,
        )
        registerScreen(
            onNavigateToMain = navController::onNavigateToMainGraph,
            onNavigateToLogin = navController::onNavigateToLogin
        )
    }
}

fun NavController.onNavigateToAuthGraph() = navigate(AuthGraph) {
    popUpTo(graph.findStartDestination().id) {
        inclusive = true
    }
}