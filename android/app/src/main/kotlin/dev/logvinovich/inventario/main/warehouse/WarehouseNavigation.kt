package dev.logvinovich.inventario.main.warehouse

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.logvinovich.inventario.main.warehouse.item.navigateToInventoryItem
import kotlinx.serialization.Serializable

@Serializable
data class WarehouseDestination(val warehouseId: Long)

fun NavGraphBuilder.warehouseScreen(navController: NavController) {
    composable<WarehouseDestination> { navBackStackEntry ->
        val warehouseId = navBackStackEntry.toRoute<WarehouseDestination>().warehouseId
        WarehouseScreen(
            warehouseId = warehouseId,
            onNavigateToInventoryItem = {
                navController.navigateToInventoryItem(warehouseId, null)
            },
            savedItem = null
        )
    }
}

fun NavController.navigateToWarehouse(warehouseId: Long) =
    navigate(WarehouseDestination(warehouseId))