package dev.logvinovich.inventario.main.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.domain.model.Warehouse

@Composable
fun ManagerDrawerContent(
    warehouses: List<Warehouse>,
    selectedWarehouseId: Long,
    onWarehouseClick: (Long) -> Unit,
    onLogoutManager: () -> Unit,
    onSaleNavigate: () -> Unit,
    onChatNavigate: () -> Unit
) {
    ModalDrawerSheet {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item(key = "Drawer top bar") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.warehouses),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(onClick = onLogoutManager) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout"
                        )
                    }
                }

                HorizontalDivider()
            }

            items(
                items = warehouses,
                key = { warehouse -> warehouse.id }
            ) { warehouse ->
                NavigationDrawerItem(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .animateItem(),
                    label = {
                        Text(warehouse.name)
                    },
                    selected = warehouse.id == selectedWarehouseId,
                    onClick = { onWarehouseClick(warehouse.id) }
                )
            }

            item(key = "Sale navigate") {
                HorizontalDivider(modifier = Modifier.padding(bottom = 10.dp))

                NavigationDrawerItem(
                    selected = false,
                    label = {
                        Text(
                            text = stringResource(R.string.sales),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Sales")
                    },
                    onClick = onSaleNavigate
                )
            }

            item(key = "Chat navigate") {
                NavigationDrawerItem(
                    selected = false,
                    label = {
                        Text(
                            text = stringResource(R.string.chat),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Chat,
                            contentDescription = "chat"
                        )
                    },
                    onClick = onChatNavigate
                )
            }
        }
    }
}