package dev.logvinovich.inventario.main.admin

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.auth.ui.onNavigateToAuthGraph
import dev.logvinovich.inventario.domain.model.Product
import dev.logvinovich.inventario.main.admin.navigation.AdminSection
import dev.logvinovich.inventario.main.admin.navigation.adminNavigationItems
import dev.logvinovich.inventario.main.admin.product.ProductsContent
import dev.logvinovich.inventario.main.admin.product.details.navigateToProductDetails
import dev.logvinovich.inventario.main.admin.stats.navigateToStats
import dev.logvinovich.inventario.main.admin.viewmodel.AdminIntent
import dev.logvinovich.inventario.main.admin.viewmodel.AdminViewModel
import dev.logvinovich.inventario.main.admin.warehouse.WarehousesContent
import dev.logvinovich.inventario.main.chat.navigateToChat
import dev.logvinovich.inventario.main.warehouse.navigateToWarehouse
import dev.logvinovich.inventario.ui.component.ProgressCard
import dev.logvinovich.inventario.ui.util.SnackbarController
import dev.logvinovich.inventario.ui.util.UiText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdminScreen(
    navController: NavController,
    adminId: Long,
    viewModel: AdminViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var savedProduct by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("savedProduct", "")
            ?.filter { it.isNotBlank() }
            ?.collectLatest { productJson ->
                savedProduct = Json.decodeFromString<Product>(productJson)

                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<String>("savedProduct")
            }
    }

    LaunchedEffect(uiState.uiMessage) {
        uiState.uiMessage?.let { msg ->
            when (msg) {
                is UiText.Plain -> SnackbarController.sendMessageEvent(msg.message)
                is UiText.Resource -> SnackbarController.sendMessageResEvent(msg.messageRes)
            }
        }
        viewModel.handleIntent(AdminIntent.ClearUiMessage)
    }

    ProgressCard(isLoading = uiState.loading)

    AnimatedVisibility(
        visible = uiState.organizations.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AdminDrawerContent(
                    viewModel = viewModel,
                    organizations = uiState.organizations,
                    onCloseDrawer = { scope.launch { drawerState.close() } },
                    selectedOrganizationId = requireNotNull(uiState.selectedOrganizationId),
                    organizationName = uiState.organizationName,
                    organizationNameHasError = uiState.organizationNameHasError,
                    showOrganizationDialog = uiState.showOrganizationDialog,
                    onStatsNavigate = { navController.navigateToStats() }
                )
            }
        ) {
            Scaffold(
                modifier = Modifier,
                topBar = {
                    TopAppBar(
                        title = {
                            AnimatedContent(
                                targetState = uiState.currentSection,
                                transitionSpec = {
                                    (slideInVertically { it } + fadeIn()).togetherWith(
                                        slideOutVertically { -it } + fadeOut())
                                },
                                label = "Title transition"
                            ) { target ->
                                Text(
                                    text = when (target) {
                                        AdminSection.WAREHOUSES -> stringResource(R.string.warehouses)
                                        AdminSection.PRODUCTS -> stringResource(R.string.products)
                                        AdminSection.CHAT -> stringResource(R.string.chat)
                                    }
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    viewModel.handleIntent(AdminIntent.Logout)
                                    navController.onNavigateToAuthGraph()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.Logout,
                                    contentDescription = "Admin logout"
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Admin menu icon"
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar {
                        adminNavigationItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = uiState.selectedNavigationIndex == index,
                                onClick = {
                                    if (item.section == AdminSection.CHAT) {
                                        navController.navigateToChat(
                                            userId = adminId,
                                            organizationId = requireNotNull(uiState.selectedOrganizationId),
                                        )
                                    } else {
                                        viewModel.handleIntent(
                                            AdminIntent.ChangeSection(item.section, index)
                                        )
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.section.name
                                    )
                                },
                                label = {
                                    Text(text = stringResource(item.title))
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.surface,
                                    indicatorColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                },
                contentWindowInsets = WindowInsets(0)
            ) { contentPadding ->
                AnimatedContent(
                    modifier = Modifier.padding(contentPadding),
                    targetState = uiState.currentSection
                ) { targetSection ->
                    when (targetSection) {
                        AdminSection.WAREHOUSES -> {
                            WarehousesContent(
                                uiState = uiState,
                                viewModel = viewModel,
                                onNavigateToWarehouse = { navController.navigateToWarehouse(it) }
                            )
                        }

                        AdminSection.PRODUCTS -> {
                            ProductsContent(
                                organizationId = requireNotNull(uiState.selectedOrganizationId),
                                savedProduct = savedProduct,
                                onNavigateToProductDetails = { product, organizationId ->
                                    navController.navigateToProductDetails(organizationId, product)
                                },
                                onUpdateLoading = {
                                    viewModel.handleIntent(AdminIntent.UpdateLoading(it))
                                }
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    EmptyOrganizationListContent(
        visible = uiState.organizations.isEmpty() && !uiState.loading,
        organizationName = uiState.organizationName,
        onUpdateOrganizationName = {
            viewModel.handleIntent(AdminIntent.UpdateOrganizationName(it))
        },
        onClearOrganizationName = {
            viewModel.handleIntent(AdminIntent.ClearOrganizationName)
        },
        organizationNameHasError = uiState.organizationNameHasError,
        onCreateOrganization = {
            viewModel.handleIntent(AdminIntent.CreateOrganization)
        }
    )
}