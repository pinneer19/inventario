package dev.logvinovich.inventario.main.sale.item

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.logvinovich.inventario.main.sale.item.viewmodel.SaleItemViewModel
import kotlinx.serialization.Serializable

@Serializable
data class SaleDestination(val warehouseId: Long)

fun NavGraphBuilder.saleItemScreen(onNavigateUp: () -> Unit) {
    composable<SaleDestination> {
        val warehouseId = it.toRoute<SaleDestination>().warehouseId
        val viewModel = hiltViewModel<SaleItemViewModel, SaleItemViewModel.Factory>(
            creationCallback = { factory -> factory.create(warehouseId) }
        )
        SaleItemScreen(
            viewModel = viewModel,
            onNavigateUp = onNavigateUp
        )
    }
}

fun NavController.navigateToSaleItemScreen(warehouseId: Long) =
    navigate(SaleDestination(warehouseId))