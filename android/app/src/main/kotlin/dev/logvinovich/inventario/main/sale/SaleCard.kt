package dev.logvinovich.inventario.main.sale

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.domain.model.Sale
import dev.logvinovich.inventario.main.admin.product.SwipeableItem
import dev.logvinovich.inventario.ui.util.timeFormatter
import kotlinx.datetime.format

@Composable
fun SaleCard(
    sale: Sale,
    onDeleteSale: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.clickable { expanded = !expanded }) {
            SwipeableItem(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                onDelete = onDeleteSale
            ) {
                ListItem(
                    headlineContent = { Text(text = sale.date.time.format(timeFormatter)) },
                    supportingContent = {
                        Text(text = stringResource(R.string.total_amount, sale.totalAmount))
                    },
                    trailingContent = {
                        Text(text = "Items count: ${sale.items.size}")
                    }
                )
            }

            if (expanded) {
                HorizontalDivider()
                sale.items.forEach { item ->
                    ListItem(
                        headlineContent = { Text(text = item.productName) },
                        supportingContent = {
                            Text(
                                text = stringResource(
                                    R.string.item_quantity_price,
                                    item.quantity,
                                    item.price
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}