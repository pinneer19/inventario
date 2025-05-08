package dev.logvinovich.inventario.main.warehouse

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.logvinovich.inventario.main.warehouse.viewmodel.WarehouseViewModel
import kotlinx.serialization.Serializable

@Serializable
data class WarehouseDestination(val warehouseId: Long)

fun NavGraphBuilder.managerScreen() {
    composable<WarehouseDestination> { navBackStackEntry ->
        val warehouseId = navBackStackEntry.toRoute<WarehouseDestination>().warehouseId
        val viewModel = hiltViewModel<WarehouseViewModel, WarehouseViewModel.Factory>(
            creationCallback = { factory -> factory.create(warehouseId) }
        )
        WarehouseScreen(viewModel)
    }
}

fun NavController.navigateToWarehouse() = navigate(WarehouseDestination) {
    popUpTo(WarehouseDestination) {
        inclusive = true
    }
}