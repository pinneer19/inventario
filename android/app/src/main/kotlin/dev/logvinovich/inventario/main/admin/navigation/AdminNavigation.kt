package dev.logvinovich.inventario.main.admin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.logvinovich.inventario.main.RoutingScreen
import dev.logvinovich.inventario.main.admin.AdminScreen
import kotlinx.serialization.Serializable

@Serializable
data class AdminDestination(val adminId: Long)

fun NavGraphBuilder.adminScreen(
    navController: NavController
) {
    composable<AdminDestination> {
        val adminId = it.toRoute<AdminDestination>().adminId
        AdminScreen(navController = navController, adminId = adminId)
    }
}

fun NavController.navigateToAdmin(adminId: Long) = navigate(AdminDestination(adminId)) {
    popUpTo(RoutingScreen) { inclusive = true }
    launchSingleTop = true
}