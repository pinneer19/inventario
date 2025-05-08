package dev.logvinovich.inventario.main.admin

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.logvinovich.domain.model.Product
import dev.logvinovich.inventario.main.admin.navigation.AdminSection
import dev.logvinovich.inventario.main.admin.navigation.adminNavigationItems
import dev.logvinovich.inventario.main.admin.product.ProductsContent
import dev.logvinovich.inventario.main.admin.product.details.navigateToProductDetails
import dev.logvinovich.inventario.main.admin.viewmodel.AdminIntent
import dev.logvinovich.inventario.main.admin.viewmodel.AdminViewModel
import dev.logvinovich.inventario.ui.component.ProgressCard
import dev.logvinovich.inventario.ui.util.SnackbarController
import dev.logvinovich.inventario.ui.util.UiText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdminScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentSection by rememberSaveable { mutableStateOf(AdminSection.WAREHOUSES) }
    var savedProduct by remember { mutableStateOf<Product?>(null) }
    val selectedNavigationIndex = rememberSaveable { mutableIntStateOf(0) }

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
                    organizations = uiState.organizations,
                    onOrganizationClick = { organizationId ->
                        viewModel.handleIntent(AdminIntent.SelectOrganization(organizationId))
                        scope.launch { drawerState.close() }
                    },
                    selectedOrganizationId = requireNotNull(uiState.selectedOrganizationId),
                    organizationName = uiState.organizationName,
                    onUpdateOrganizationName = {
                        viewModel.handleIntent(AdminIntent.UpdateOrganizationName(it))
                    },
                    organizationNameHasError = uiState.organizationNameHasError,
                    onCreateOrganization = {
                        viewModel.handleIntent(AdminIntent.DismissOrganizationDialog)
                        viewModel.handleIntent(AdminIntent.CreateOrganization)
                    },
                    onShowOrganizationDialog = {
                        viewModel.handleIntent(AdminIntent.ShowOrganizationDialog)
                    },
                    onDismissOrganizationDialog = {
                        viewModel.handleIntent(AdminIntent.DismissOrganizationDialog)
                    },
                    showOrganizationDialog = uiState.showOrganizationDialog
                )
            }
        ) {
            Scaffold(
                modifier = Modifier,
                bottomBar = {
                    NavigationBar {
                        adminNavigationItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedNavigationIndex.intValue == index,
                                onClick = {
                                    selectedNavigationIndex.intValue = index
                                    currentSection = item.section
                                },
                                icon = {
                                    Icon(imageVector = item.icon, contentDescription = item.title)
                                },
                                label = {
                                    Text(text = item.title)
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
                    targetState = currentSection
                ) { targetSection ->
                    when (targetSection) {
                        AdminSection.WAREHOUSES -> {
                            AdminMainContent(
                                uiState = uiState,
                                viewModel = viewModel,
                                onOpenDrawer = {
                                    scope.launch { drawerState.open() }
                                }
                            )
                        }

                        AdminSection.CHAT -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("hello")
                            }
                        }

                        AdminSection.PRODUCTS -> {
                            ProductsContent(
                                organizationId = requireNotNull(uiState.selectedOrganizationId),
                                savedProduct = savedProduct,
                                onOpenDrawer = { scope.launch { drawerState.open() } },
                                onNavigateToProductDetails = { product, organizationId ->
                                    navController.navigateToProductDetails(organizationId, product)
                                },
                                onUpdateLoading = {
                                    viewModel.handleIntent(AdminIntent.UpdateLoading(it))
                                }
                            )
                        }
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