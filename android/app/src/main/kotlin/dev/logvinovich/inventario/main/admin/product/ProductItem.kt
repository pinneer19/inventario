package dev.logvinovich.inventario.main.admin.product

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.logvinovich.domain.model.Product
import kotlin.math.roundToInt

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onDeleteProduct: () -> Unit,
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            when (state) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDeleteProduct()
                    true
                }

                else -> false
            }
        }
    )

    SwipeToDismissBox(
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        state = swipeToDismissBoxState,
        backgroundContent = {
            val (alignment: Alignment, icon: ImageVector?) =
                when (swipeToDismissBoxState.dismissDirection) {
                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd to Icons.Outlined.Delete
                    SwipeToDismissBoxValue.Settled, SwipeToDismissBoxValue.StartToEnd ->
                        Alignment.Center to null
                }

            val iconOffsetX = remember { Animatable(0f) }

            LaunchedEffect(swipeToDismissBoxState.dismissDirection) {
                val initialX = when (swipeToDismissBoxState.dismissDirection) {
                    SwipeToDismissBoxValue.Settled, SwipeToDismissBoxValue.StartToEnd -> 0f
                    SwipeToDismissBoxValue.EndToStart -> 500f
                }

                iconOffsetX.snapTo(initialX)

                val targetX = 0f

                iconOffsetX.animateTo(
                    targetValue = targetX,
                    animationSpec = tween(durationMillis = 400)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = alignment
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.offset {
                            IntOffset(x = iconOffsetX.value.roundToInt(), y = 0)
                        },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    ) {
        ProductCard(
            product = product,
            onClick = onClick,
        )
    }
}