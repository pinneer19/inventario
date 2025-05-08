package dev.logvinovich.inventario.main

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.logvinovich.domain.model.Product
import dev.logvinovich.domain.model.Role
import dev.logvinovich.inventario.main.admin.navigation.adminScreen
import dev.logvinovich.inventario.main.admin.navigation.navigateToAdmin
import dev.logvinovich.inventario.main.admin.product.details.productDetailsScreen
import dev.logvinovich.inventario.main.manager.managerScreen
import dev.logvinovich.inventario.main.manager.navigateToManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class MainGraph(val role: Role)

@Serializable
private data object RoutingScreen

fun NavGraphBuilder.mainNavigation(navController: NavController) {
    navigation<MainGraph>(
        startDestination = RoutingScreen
    ) {
        composable<RoutingScreen> {
            val userRole = it.arguments?.getSerializable("role", Role::class.java) ?: Role.MANAGER
            LaunchedEffect(userRole) {
                when (userRole) {
                    Role.ADMIN -> navController.navigateToAdmin()
                    Role.MANAGER -> navController.navigateToManager()
                }
            }
        }
        adminScreen(navController)
        managerScreen()
        productDetailsScreen(navController)
    }
}

fun NavController.onNavigateToMainGraph(role: Role) = navigate(MainGraph(role)) {
    popUpTo(graph.findStartDestination().id) {
        inclusive = true
    }
}