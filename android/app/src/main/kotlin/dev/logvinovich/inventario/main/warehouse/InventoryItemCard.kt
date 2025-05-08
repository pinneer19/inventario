package dev.logvinovich.inventario.main.warehouse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.logvinovich.domain.model.InventoryItem
import dev.logvinovich.inventario.R

@Composable
fun InventoryItemCard(
    item: InventoryItem,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.product.imageUrl)
                    .crossfade(true).build(),
                contentDescription = "${item.product.name} image",
                loading = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_broken_image),
                            contentDescription = "Error image",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                },
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = item.product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_barcode),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = item.product.barcode,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(Modifier.height(4.dp))

                Row {
                    Text(
                        text = "Price: $${item.price}",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Qty: ${item.quantity}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            if (onClick != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = "Open item",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}