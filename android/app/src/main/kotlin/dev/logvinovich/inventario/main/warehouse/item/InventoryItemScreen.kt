package dev.logvinovich.inventario.main.warehouse.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.main.warehouse.item.viewmodel.InventoryItemIntent
import dev.logvinovich.inventario.main.warehouse.item.viewmodel.InventoryItemViewModel
import dev.logvinovich.inventario.ui.component.ProgressCard
import dev.logvinovich.inventario.ui.component.TextFieldComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryItemScreen(
    viewModel: InventoryItemViewModel,
    onNavigateUp: (InventoryItem?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.saveResult) {
        uiState.saveResult?.let { savedProduct ->
            onNavigateUp(savedProduct)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            if (uiState.isEdit) R.string.save_item else R.string.add_item
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp(null) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars
    ) { contentPadding ->
        ProgressCard(isLoading = uiState.loading, modifier = Modifier.zIndex(1f))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.select_product),
                style = MaterialTheme.typography.titleLarge
            )

            ProductPicker(
                modifier = Modifier.fillMaxWidth(),
                products = uiState.organizationProducts,
                selectedIndex = uiState.selectedProductIndex,
                onProductSelect = {
                    viewModel.handleIntent(InventoryItemIntent.UpdateSelectedIndex(it))
                }
            )

            TextFieldComponent(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = uiState.price,
                onValueChange = { viewModel.handleIntent(InventoryItemIntent.UpdatePrice(it)) },
                label = stringResource(R.string.price),
                isError = uiState.priceError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = "Inventory item price"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Decimal
                ),
                placeholder = stringResource(R.string.enter_item_price)
            )

            TextFieldComponent(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = uiState.quantity,
                onValueChange = { viewModel.handleIntent(InventoryItemIntent.UpdateQuantity(it)) },
                label = stringResource(R.string.quantity),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Inventory item quantity"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.handleIntent(InventoryItemIntent.SaveItem)
                }),
                placeholder = stringResource(R.string.enter_item_quantity)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = { viewModel.handleIntent(InventoryItemIntent.SaveItem) }
            ) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}