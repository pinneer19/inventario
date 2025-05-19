package dev.logvinovich.inventario.main.sale.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.main.sale.item.viewmodel.SaleItemIntent
import dev.logvinovich.inventario.main.sale.item.viewmodel.SaleItemViewModel
import dev.logvinovich.inventario.ui.component.TextFieldComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleItemScreen(
    viewModel: SaleItemViewModel,
    onNavigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.saleCompleted) {
        if (uiState.saleCompleted) onNavigateUp()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_sale)) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InventoryItemPicker(
                inventoryItems = uiState.availableItems,
                selectedIndex = uiState.selectedItemIndex,
                onItemSelect = { viewModel.handleIntent(SaleItemIntent.SelectInventoryItem(it)) }
            )

            TextFieldComponent(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = uiState.quantity,
                onValueChange = { viewModel.handleIntent(SaleItemIntent.UpdateQuantity(it)) },
                label = stringResource(R.string.quantity),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.quantityError,
                placeholder = stringResource(R.string.enter_item_quantity),
                supportingText = uiState.availableQuantity?.let {
                    stringResource(R.string.available_quantity, it.toInt())
                }
            )

            TextFieldComponent(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = uiState.price,
                onValueChange = { viewModel.handleIntent(SaleItemIntent.UpdatePrice(it)) },
                label = stringResource(R.string.price),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.priceError,
                placeholder = stringResource(R.string.enter_item_quantity)
            )

            Button(
                onClick = { viewModel.handleIntent(SaleItemIntent.AddItemToSale) },
                modifier = Modifier.fillMaxWidth(0.6f),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSurface),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_sale),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(start = 25.dp, end = 25.dp, top = 10.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.saleItems) { item ->
                    ListItem(
                        headlineContent = { Text(item.productName) },
                        supportingContent = {
                            Text(
                                text = stringResource(
                                    R.string.item_quantity_price,
                                    item.quantity,
                                    item.price
                                )
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = {
                                viewModel.handleIntent(SaleItemIntent.RemoveItem(item))
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove item")
                            }
                        }
                    )
                }

                if (uiState.saleItems.isEmpty()) {
                    item {
                        Text(text = stringResource(R.string.no_items))
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = { viewModel.handleIntent(SaleItemIntent.SubmitSale) }
            ) {
                Text(
                    text = stringResource(R.string.submit_sale),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}