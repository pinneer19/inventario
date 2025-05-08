package dev.logvinovich.inventario.main.admin.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.logvinovich.inventario.main.admin.AdminScreen
import dev.logvinovich.inventario.main.admin.product.details.navigateToProductDetails
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data object AdminDestination

fun NavGraphBuilder.adminScreen(navController: NavController) {
    composable<AdminDestination> { navBackStackEntry ->
        AdminScreen(navController = navController)
    }
}

fun NavController.navigateToAdmin() = navigate(AdminDestination) {
    popUpTo(AdminDestination) {
        inclusive = true
    }
}