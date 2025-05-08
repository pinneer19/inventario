package dev.logvinovich.inventario.main.manager

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ManagerDestination

fun NavGraphBuilder.managerScreen() {
    composable<ManagerDestination> {
        ManagerScreen()
    }
}

fun NavController.navigateToManager() = navigate(ManagerDestination) {
    popUpTo(ManagerDestination) {
        inclusive = true
    }
}