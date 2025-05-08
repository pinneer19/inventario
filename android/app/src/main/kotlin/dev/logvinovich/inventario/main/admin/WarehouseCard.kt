package dev.logvinovich.inventario.main.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.logvinovich.domain.model.User
import dev.logvinovich.domain.model.Warehouse

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WarehouseCard(
    warehouse: Warehouse,
    onAssignManager: () -> Unit,
    onDeleteWarehouse: () -> Unit,
    onUnassignManager: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    var displayManagers by remember { mutableStateOf(false) }
    val animatedCornerDp by animateDpAsState(targetValue = if (displayManagers) 0.dp else 12.dp)
    val animatedIconRotation by animateFloatAsState(
        targetValue = if (displayManagers) 180f else 0f,
        animationSpec = tween(500)
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(60.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = 12.dp,
                            bottomEnd = animatedCornerDp,
                            bottomStart = animatedCornerDp
                        )
                    )
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .clickable { }
                    .padding(10.dp)
            ) {
                LookaheadScope {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .animateBounds(this),
                        text = warehouse.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    AnimatedVisibility(
                        visible = warehouse.managers.isNotEmpty(),
                        enter = slideInHorizontally { it / 2 } + fadeIn(),
                        exit = slideOutHorizontally { it / 2 } + fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) { displayManagers = !displayManagers }
                                .rotate(animatedIconRotation)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Display managers",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(warehouse.managers.isNotEmpty() && displayManagers) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(start = 15.dp, end = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    warehouse.managers.forEach { manager ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = manager.username,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Box(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        onUnassignManager(manager.id)
                                    }
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Unassign manager",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }


        Row(
            modifier = Modifier
                .height(60.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onAssignManager) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Add Manager",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            IconButton(onClick = onDeleteWarehouse) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete warehouse",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WarehouseCardPreview() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            WarehouseCard(
                warehouse = Warehouse(
                    id = -1,
                    name = "Warehouse 1",
                    organizationId = -1,
                    managers = listOf(
                        User(username = "Manager 1", id = 1),
                        User(username = "Manager 2", id = 2),
                    )
                ),
                onAssignManager = {},
                onUnassignManager = {},
                onDeleteWarehouse = {}
            )
        }
    }
}