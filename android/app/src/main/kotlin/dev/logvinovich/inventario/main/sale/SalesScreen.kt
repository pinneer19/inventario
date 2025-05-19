package dev.logvinovich.inventario.main.sale

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.main.sale.viewmodel.SalesIntent
import dev.logvinovich.inventario.main.sale.viewmodel.SalesViewModel
import dev.logvinovich.inventario.ui.component.CustomFab
import dev.logvinovich.inventario.ui.component.ProgressCard
import dev.logvinovich.inventario.ui.util.dateFormatter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.datetime.format

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun SalesScreen(
    viewModel: SalesViewModel = hiltViewModel(),
    onNavigateToSaleItem: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterFlow = snapshotFlow { uiState.dateRange }
        .combine(snapshotFlow { uiState.selectedWarehouseId }) { dateRange, warehouseId ->
            dateRange to warehouseId
        }

    LaunchedEffect(Unit) {
        filterFlow
            .debounce(500)
            .collectLatest {
                viewModel.handleIntent(SalesIntent.GetSales)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.sales)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            CustomFab(
                modifier = Modifier.padding(bottom = 30.dp, end = 10.dp),
                onClick = onNavigateToSaleItem,
                contentDescription = "Sales fab"
            )
        },
        contentWindowInsets = WindowInsets.statusBars
    ) { padding ->
        ProgressCard(isLoading = uiState.loading, modifier = Modifier.zIndex(1f))

        Column(modifier = Modifier.padding(padding)) {
            AnimatedVisibility(
                visible = !uiState.initialDataIsEmpty,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    FilterRow(
                        warehouses = uiState.warehouses,
                        selectedWarehouseId = uiState.selectedWarehouseId,
                        onWarehouseChange = {
                            viewModel.handleIntent(
                                SalesIntent.SetWarehouseFilter(it)
                            )
                        },
                        dateRange = uiState.dateRange,
                        onDateRangeChange = {
                            viewModel.handleIntent(
                                SalesIntent.SetDateRangeFilter(it)
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiState.groupedSales.forEach { (date, sales) ->
                    stickyHeader {
                        Text(
                            text = date.format(dateFormatter),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    items(
                        items = sales,
                        key = { it.id }
                    ) { sale ->
                        SaleCard(
                            sale = sale,
                            modifier = Modifier.animateItem(),
                            onDeleteSale = {
                                viewModel.handleIntent(SalesIntent.DeleteSale(sale.id))
                            }
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = uiState.groupedSales.isEmpty() && !uiState.loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.no_sales_found),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun SelectionSample() {
    SelectionContainer {
        Column {
            Text("Text 1")
            Text("Text 2")
            Text("טקסט 3")
        }
    }
}
