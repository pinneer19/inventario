package dev.logvinovich.inventario.main.chat

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.logvinovich.inventario.main.chat.viewmodel.ChatViewModel
import kotlinx.serialization.Serializable

@Serializable
data class ChatDestination(val userId: Long, val organizationId: Long?, val warehouseId: Long?)

fun NavGraphBuilder.chatScreen(navController: NavController) {
    composable<ChatDestination> {
        val (userId, organizationId, warehouseId) = it.toRoute<ChatDestination>()
        val viewModel = hiltViewModel<ChatViewModel, ChatViewModel.Factory>(
            creationCallback = { factory -> factory.create(userId, organizationId, warehouseId) }
        )
        ChatScreen(
            viewModel = viewModel,
            onNavigateUp = navController::navigateUp
        )
    }
}

fun NavController.navigateToChat(
    userId: Long,
    organizationId: Long? = null,
    warehouseId: Long? = null
) =
    navigate(ChatDestination(userId, organizationId, warehouseId))