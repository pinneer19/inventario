package dev.logvinovich.inventario.main

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.logvinovich.inventario.domain.model.Role
import dev.logvinovich.inventario.auth.model.UserData
import dev.logvinovich.inventario.main.admin.navigation.adminScreen
import dev.logvinovich.inventario.main.admin.navigation.navigateToAdmin
import dev.logvinovich.inventario.main.admin.product.details.productDetailsScreen
import dev.logvinovich.inventario.main.admin.stats.statisticScreen
import dev.logvinovich.inventario.main.chat.chatScreen
import dev.logvinovich.inventario.main.manager.managerScreen
import dev.logvinovich.inventario.main.manager.navigateToManager
import dev.logvinovich.inventario.main.sale.item.saleItemScreen
import dev.logvinovich.inventario.main.sale.salesScreen
import dev.logvinovich.inventario.main.warehouse.item.inventoryItemScreen
import dev.logvinovich.inventario.main.warehouse.warehouseScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class MainGraph(val userDataJson: String)

@Serializable
data object RoutingScreen

fun NavGraphBuilder.mainNavigation(navController: NavController) {
    navigation<MainGraph>(
        startDestination = RoutingScreen
    ) {
        composable<RoutingScreen> {
            val userData = Json.decodeFromString<UserData>(
                requireNotNull(it.arguments?.getString("userDataJson"))
            )
            LaunchedEffect(userData) {
                when (userData.role) {
                    Role.ADMIN -> navController.navigateToAdmin(userData.id)
                    Role.MANAGER -> navController.navigateToManager(userData.id)
                }
            }
        }
        adminScreen(navController)
        managerScreen(navController)
        warehouseScreen(navController)
        productDetailsScreen(navController)
        inventoryItemScreen(navController)
        salesScreen(navController)
        saleItemScreen(navController::navigateUp)
        chatScreen(navController)
        statisticScreen(navController)
    }
}

fun NavController.onNavigateToMainGraph(userDataJson: String) = navigate(MainGraph(userDataJson)) {
    popUpTo(graph.findStartDestination().id) {
        inclusive = true
    }
}