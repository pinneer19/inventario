package dev.logvinovich.inventario.main.manager

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import dev.logvinovich.inventario.auth.ui.onNavigateToAuthGraph
import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.main.chat.navigateToChat
import dev.logvinovich.inventario.main.manager.viewmodel.ManagerIntent
import dev.logvinovich.inventario.main.manager.viewmodel.ManagerViewModel
import dev.logvinovich.inventario.main.sale.navigateToSalesScreen
import dev.logvinovich.inventario.main.warehouse.WarehouseScreen
import dev.logvinovich.inventario.main.warehouse.item.navigateToInventoryItem
import dev.logvinovich.inventario.ui.component.ProgressCard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerScreen(
    navController: NavController,
    managerId: Long,
    viewModel: ManagerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var savedItem by remember { mutableStateOf<InventoryItem?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("savedItem", "")
            ?.filter { it.isNotBlank() }
            ?.collectLatest { itemJson ->
                savedItem = Json.decodeFromString<InventoryItem>(itemJson)

                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<String>("savedItem")
            }
    }

    ProgressCard(isLoading = uiState.loading)

    AnimatedVisibility(
        visible = uiState.warehouses.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ManagerDrawerContent(
                    warehouses = uiState.warehouses,
                    selectedWarehouseId = requireNotNull(uiState.selectedWarehouseId),
                    onWarehouseClick = {
                        viewModel.handleIntent(ManagerIntent.UpdateSelectedWarehouse(it))
                        scope.launch { drawerState.close() }
                    },
                    onLogoutManager = {
                        viewModel.handleIntent(ManagerIntent.Logout)
                        navController.onNavigateToAuthGraph()
                    },
                    onSaleNavigate = {
                        navController.navigateToSalesScreen(requireNotNull(uiState.selectedWarehouseId))
                    },
                    onChatNavigate = {
                        navController.navigateToChat(
                            userId = managerId,
                            warehouseId = uiState.selectedWarehouseId
                        )
                    }
                )
            }
        ) {
            Scaffold(
                contentWindowInsets = WindowInsets(0)
            ) {
                val warehouseId = requireNotNull(uiState.selectedWarehouseId)
                WarehouseScreen(
                    warehouseId = warehouseId,
                    onNavigateToInventoryItem = { item ->
                        navController.navigateToInventoryItem(warehouseId, item)
                    },
                    onExpandDrawer = {
                        scope.launch { drawerState.open() }
                    },
                    savedItem = savedItem
                )
            }
        }
    }

    EmptyWarehouseListContent(
        visible = uiState.warehouses.isEmpty() && !uiState.loading,
        onRefresh = { viewModel.handleIntent(ManagerIntent.GetWarehouses) }
    )
}