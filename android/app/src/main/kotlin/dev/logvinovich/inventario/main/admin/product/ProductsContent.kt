package dev.logvinovich.inventario.main.admin.product

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.logvinovich.inventario.domain.model.Product
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.main.admin.product.viewmodel.ProductsIntent
import dev.logvinovich.inventario.main.admin.product.viewmodel.ProductsViewModel
import dev.logvinovich.inventario.ui.component.CustomFab
import dev.logvinovich.inventario.ui.component.ProgressCard

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsContent(
    organizationId: Long,
    savedProduct: Product?,
    onNavigateToProductDetails: (Product?, Long) -> Unit,
    onUpdateLoading: (Boolean) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loading) {
        onUpdateLoading(uiState.loading)
    }

    LaunchedEffect(organizationId) {
        viewModel.handleIntent(ProductsIntent.SetOrganizationId(organizationId))
    }

    LaunchedEffect(savedProduct) {
        savedProduct?.let {
            viewModel.handleIntent(ProductsIntent.SetSavedProduct(it))
        }
    }

    ProgressCard(isLoading = uiState.loading)

    Scaffold(
        floatingActionButton = {
            CustomFab(
                contentDescription = "Add products",
                onClick = {
                    onNavigateToProductDetails(null, organizationId)
                }
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
                items = uiState.products,
                key = { product -> product.id }
            ) { product ->
                SwipeableItem(
                    modifier = Modifier.animateItem(),
                    onDelete = {
                        viewModel.handleIntent(ProductsIntent.DeleteProduct(product.id))
                    }
                ) {
                    ProductCard(
                        product = product,
                        onClick = {
                            onNavigateToProductDetails(product, organizationId)
                        },
                    )
                }
            }

            if (uiState.products.isEmpty() && !uiState.loading) {
                item(key = "Empty product list") {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.no_products),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                        )

                        TextButton(
                            onClick = {
                                onNavigateToProductDetails(null, organizationId)
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.add_product),
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}