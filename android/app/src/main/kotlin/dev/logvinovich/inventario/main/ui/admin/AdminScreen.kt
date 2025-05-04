package dev.logvinovich.inventario.main.ui.admin

import android.R.id.message
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dev.logvinovich.inventario.main.ui.admin.viewmodel.AdminIntent
import dev.logvinovich.inventario.main.ui.admin.viewmodel.AdminViewModel
import dev.logvinovich.inventario.ui.component.ProgressCard
import dev.logvinovich.inventario.ui.util.SnackbarController
import dev.logvinovich.inventario.ui.util.UiText
import kotlinx.coroutines.launch

@Composable
fun AdminScreen(viewModel: AdminViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.uiMessage) {
        uiState.uiMessage?.let { msg ->
            when(msg) {
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
            AdminMainContent(
                uiState = uiState,
                viewModel = viewModel,
                onOpenDrawer = {
                    scope.launch { drawerState.open() }
                }
            )
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

@Preview
@Composable
fun AdminScreenPreview() {
    AdminScreen()
}