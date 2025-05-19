package dev.logvinovich.inventario.main.sale

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.logvinovich.inventario.main.sale.item.navigateToSaleItemScreen
import kotlinx.serialization.Serializable

@Serializable
data class SalesDestination(val warehouseId: Long)

fun NavGraphBuilder.salesScreen(navController: NavController) {
    composable<SalesDestination> {
        val warehouseId = it.toRoute<SalesDestination>().warehouseId
        SalesScreen(
            onNavigateUp = navController::navigateUp,
            onNavigateToSaleItem = {
                navController.navigateToSaleItemScreen(warehouseId)
            }
        )
    }
}

fun NavController.navigateToSalesScreen(warehouseId: Long) = navigate(SalesDestination(warehouseId))