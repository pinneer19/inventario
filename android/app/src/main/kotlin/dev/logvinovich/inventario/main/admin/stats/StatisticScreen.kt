package dev.logvinovich.inventario.main.admin.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.main.admin.stats.viewmodel.StatsViewModel
import dev.logvinovich.inventario.ui.component.ProgressCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticScreen(viewModel: StatsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.stats))
            })
        },
        contentWindowInsets = WindowInsets(0)
    ) { contentPadding ->
        ProgressCard(isLoading = uiState.loading)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 20.dp)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 25.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.total_sales),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "\$${uiState.stats.totalSalesAmount}",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.total_transactions),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${uiState.stats.totalSalesCount}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.sales_over_time),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(modifier = Modifier.padding(8.dp)) {
                        SalesLineChart(data = uiState.stats.salesByDate)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.top_warehouses),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                uiState.stats.topWarehouses.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        ListItem(
                            headlineContent = { Text(item.warehouseName) },
                            supportingContent = { Text("Sales: \$${item.totalAmount}") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    }
                }
            }


            item {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.top_products),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                uiState.stats.topProducts.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        ListItem(
                            headlineContent = { Text(item.productName) },
                            supportingContent = { Text("Sales: \$${item.totalAmount}") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    }
                }
            }
        }
    }
}