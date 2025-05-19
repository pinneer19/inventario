package dev.logvinovich.inventario.main.manager

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.logvinovich.inventario.main.RoutingScreen
import dev.logvinovich.inventario.main.manager.viewmodel.ManagerViewModel
import kotlinx.serialization.Serializable

@Serializable
data class ManagerDestination(val managerId: Long)

fun NavGraphBuilder.managerScreen(
    navController: NavController
) {
    composable<ManagerDestination> {
        val managerId = it.toRoute<ManagerDestination>().managerId
        val viewModel = hiltViewModel<ManagerViewModel, ManagerViewModel.Factory>(
            creationCallback = { factory -> factory.create(managerId) }
        )
        ManagerScreen(
            navController = navController,
            managerId = managerId,
            viewModel = viewModel
        )
    }
}

fun NavController.navigateToManager(managerId: Long) = navigate(ManagerDestination(managerId)) {
    popUpTo(RoutingScreen) { inclusive = true }
    launchSingleTop = true
}