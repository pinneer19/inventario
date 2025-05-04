package dev.logvinovich.inventario.main.ui.admin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object AdminDestination

fun NavGraphBuilder.adminScreen() {
    composable<AdminDestination> {
        AdminScreen()
    }
}

fun NavController.navigateToAdmin() = navigate(AdminDestination) {
    popUpTo(AdminDestination) {
        inclusive = true
    }
}