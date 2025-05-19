package dev.logvinovich.inventario.main.warehouse

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.main.admin.product.SwipeableItem
import dev.logvinovich.inventario.main.warehouse.item.InventoryItemCard
import dev.logvinovich.inventario.main.warehouse.viewmodel.WarehouseIntent
import dev.logvinovich.inventario.main.warehouse.viewmodel.WarehouseViewModel
import dev.logvinovich.inventario.ui.component.CustomFab
import dev.logvinovich.inventario.ui.component.ProgressCard
import dev.logvinovich.inventario.util.generateBarCode
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun WarehouseScreen(
    warehouseId: Long,
    savedItem: InventoryItem?,
    onNavigateToInventoryItem: (InventoryItem?) -> Unit,
    viewModel: WarehouseViewModel = hiltViewModel(),
    onExpandDrawer: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    var showBarcode by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(warehouseId) {
        viewModel.handleIntent(WarehouseIntent.GetInventoryItems(warehouseId))
    }

    LaunchedEffect(Unit) {
        snapshotFlow { uiState.searchText }
            .debounce(500)
            .collectLatest {
                viewModel.handleIntent(WarehouseIntent.FilterItems)
            }
    }

    LaunchedEffect(savedItem) {
        savedItem?.let {
            viewModel.handleIntent(WarehouseIntent.SetSavedItem(it))
        }
    }

    ProgressCard(isLoading = uiState.loading)

    AnimatedVisibility(
        visible = uiState.inventoryItems.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.statusBars,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    onExpandDrawer?.let {
                        IconButton(
                            onClick = onExpandDrawer,
                            modifier = Modifier.padding(start = 20.dp, top = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open drawer"
                            )
                        }
                    }

                    SearchBar(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 15.dp, end = 20.dp),
                        inputField = {
                            SearchBarDefaults.InputField(
                                query = uiState.searchText,
                                onQueryChange = {
                                    viewModel.handleIntent(WarehouseIntent.UpdateSearchText(it))
                                },
                                onSearch = {

                                },
                                expanded = false,
                                onExpandedChange = { },
                                placeholder = { Text(stringResource(R.string.search)) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Search, null)
                                },
                                trailingIcon = {
                                    if (uiState.searchText.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                viewModel.handleIntent(WarehouseIntent.ClearSearchText)
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Clear search"
                                            )
                                        }
                                    }
                                })
                        },
                        expanded = false,
                        onExpandedChange = {},
                        content = {}
                    )
                }
            },
            floatingActionButton = {
                CustomFab(
                    contentDescription = "Add item",
                    modifier = Modifier.padding(bottom = 30.dp, end = 10.dp),
                    onClick = { onNavigateToInventoryItem(null) }
                )
            }
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 15.dp)
            ) {
                items(
                    items = uiState.filteredInventoryItems,
                    key = { it.id }
                ) { item ->
                    SwipeableItem(
                        modifier = Modifier.animateItem(),
                        onDelete = { viewModel.handleIntent(WarehouseIntent.DeleteItem(item.id)) }
                    ) {
                        InventoryItemCard(
                            item = item,
                            onItemClick = { onNavigateToInventoryItem(item) },
                            onShowBarcodeClick = { showBarcode = it }
                        )
                    }
                }
            }
        }

    }
    AnimatedVisibility(
        visible = uiState.inventoryItems.isEmpty() && !uiState.loading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.empty_warehouse),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
            )

            TextButton(
                onClick = { onNavigateToInventoryItem(null) }
            ) {
                Text(
                    text = stringResource(R.string.add_item),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                )
            }
        }
    }

    showBarcode?.let { barcode ->
        AlertDialog(
            onDismissRequest = { showBarcode = null },
            title = { Text(stringResource(R.string.barcode)) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    barcode.generateBarCode()?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Barcode image"
                        )
                    }
                    Text(barcode)
                }
            },
            confirmButton = {
                TextButton(onClick = { showBarcode = null }) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }
}