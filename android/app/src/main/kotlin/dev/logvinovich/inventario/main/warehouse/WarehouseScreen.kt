package dev.logvinovich.inventario.main.warehouse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.main.warehouse.viewmodel.WarehouseViewModel
import dev.logvinovich.inventario.ui.component.ProgressCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarehouseScreen(viewModel: WarehouseViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    ProgressCard(isLoading = uiState.loading)

    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = "",
                        onQueryChange = {},
                        onSearch = {

                        },
                        expanded = false,
                        onExpandedChange = { },
                        placeholder = { Text("Search") },
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Search, null)
                        }
                    )
                },

                expanded = false,
                onExpandedChange = {},
            ) {

            }
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
                items = uiState.inventoryItems,
                key = { it.id }
            ) { product ->
                InventoryItemCard(product)
            }

            item(key = "Empty inventory items list") {
                if (uiState.inventoryItems.isEmpty() && !uiState.loading) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.empty_warehouse),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                        )

                        TextButton(
                            onClick = {  }
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
}