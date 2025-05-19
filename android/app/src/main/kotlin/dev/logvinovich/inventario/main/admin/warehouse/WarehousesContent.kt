package dev.logvinovich.inventario.main.admin.warehouse

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.main.admin.viewmodel.AdminIntent
import dev.logvinovich.inventario.main.admin.viewmodel.AdminUiState
import dev.logvinovich.inventario.main.admin.viewmodel.AdminViewModel
import dev.logvinovich.inventario.main.admin.viewmodel.ConfirmDialogType
import dev.logvinovich.inventario.ui.component.CustomFab
import dev.logvinovich.inventario.ui.component.InputDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarehousesContent(
    uiState: AdminUiState,
    viewModel: AdminViewModel,
    onNavigateToWarehouse: (Long) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            CustomFab(
                contentDescription = "Add warehouse",
                onClick = { viewModel.handleIntent(AdminIntent.ShowWarehouseDialog) }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterVertically
            ),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {
            items(
                items = uiState.organizationWarehouses,
                key = { warehouse -> warehouse.id }
            ) { warehouse ->
                WarehouseCard(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(12.dp))
                        .animateItem(),
                    warehouse = warehouse,
                    onAssignManager = {
                        viewModel.handleIntent(AdminIntent.ShowAssignDialog(warehouse.id))
                    },
                    onUnassignManager = { managerId ->
                        viewModel.handleIntent(
                            AdminIntent.ShowConfirmationDialog(
                                managerId = managerId,
                                warehouseId = warehouse.id,
                                dialogType = ConfirmDialogType.UNASSIGN_MANAGER
                            )
                        )
                    },
                    onDeleteWarehouse = {
                        viewModel.handleIntent(
                            AdminIntent.ShowConfirmationDialog(
                                warehouseId = warehouse.id,
                                dialogType = ConfirmDialogType.DELETE_WAREHOUSE
                            )
                        )
                    },
                    onNavigateToWarehouse = {
                        onNavigateToWarehouse(warehouse.id)
                    }
                )
            }

            item(key = "Empty warehouse list content") {
                if (uiState.organizationWarehouses.isEmpty() && !uiState.loading) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.no_warehouses),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                        )

                        TextButton(
                            onClick = { viewModel.handleIntent(AdminIntent.ShowWarehouseDialog) }
                        ) {
                            Text(
                                text = stringResource(R.string.add_warehouse),
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.showWarehouseDialog) {
        InputDialog(
            inputValue = uiState.dialogWarehouseName,
            isError = uiState.dialogWarehouseNameHasError,
            onValueChange = { viewModel.handleIntent(AdminIntent.UpdateWarehouseName(it)) },
            onDismissRequest = { viewModel.handleIntent(AdminIntent.DismissWarehouseDialog) },
            onSubmit = {
                viewModel.handleIntent(AdminIntent.DismissWarehouseDialog)
                viewModel.handleIntent(AdminIntent.CreateWarehouse)
            },
            inputPlaceholder = stringResource(R.string.input_warehouse_name),
            title = stringResource(R.string.creating_warehouse)
        )
    }

    if (uiState.showAssignDialog) {
        InputDialog(
            submitButtonText = stringResource(R.string.assign),
            inputValue = uiState.dialogManagerUsername,
            isError = uiState.dialogManagerUsernameHasError,
            onValueChange = {
                viewModel.handleIntent(AdminIntent.UpdateManagerUsername(it))
            },
            onDismissRequest = { viewModel.handleIntent(AdminIntent.DismissAssignDialog) },
            onSubmit = { viewModel.handleIntent(AdminIntent.AssignManager) },
            inputPlaceholder = stringResource(R.string.enter_manager_username),
            title = stringResource(R.string.assigning_manager)
        )
    }

    if (uiState.showConfirmationDialog) {
        val dialogText = when (uiState.confirmationDialogType) {
            ConfirmDialogType.UNASSIGN_MANAGER -> stringResource(R.string.unassign_approve)
            ConfirmDialogType.DELETE_WAREHOUSE -> stringResource(R.string.delete_approve)
            ConfirmDialogType.NONE -> ""
        }
        val dialogActionIntent: AdminIntent? = when (uiState.confirmationDialogType) {
            ConfirmDialogType.UNASSIGN_MANAGER -> AdminIntent.UnassignManager
            ConfirmDialogType.DELETE_WAREHOUSE -> AdminIntent.DeleteWarehouse
            ConfirmDialogType.NONE -> null
        }
        AlertDialog(
            onDismissRequest = { viewModel.handleIntent(AdminIntent.DismissConfirmationDialog) },
            title = { Text(text = stringResource(R.string.request_approvement)) },
            text = {
                Text(text = dialogText)
            },
            dismissButton = {
                Button(
                    onClick = { viewModel.handleIntent(AdminIntent.DismissConfirmationDialog) }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        dialogActionIntent?.let { viewModel.handleIntent(it) }
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }
}