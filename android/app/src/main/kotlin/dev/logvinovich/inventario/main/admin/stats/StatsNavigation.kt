package dev.logvinovich.inventario.main.admin.stats

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object StatsDestination

fun NavGraphBuilder.statisticScreen(
    navController: NavController
) {
    composable<StatsDestination> {
        StatisticScreen()
    }
}

fun NavController.navigateToStats() = navigate(StatsDestination)