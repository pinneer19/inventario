package dev.logvinovich.inventario.main.admin.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.ui.graphics.vector.ImageVector
import dev.logvinovich.inventario.R

enum class AdminSection {
    WAREHOUSES, PRODUCTS, CHAT
}

data class NavigationItem(
    @StringRes val title: Int,
    val icon: ImageVector,
    val section: AdminSection
)

val adminNavigationItems = listOf(
    NavigationItem(
        title = R.string.warehouses,
        icon = Icons.Default.Warehouse,
        section = AdminSection.WAREHOUSES
    ),
    NavigationItem(
        title = R.string.products,
        icon = Icons.Default.Inventory,
        section = AdminSection.PRODUCTS
    ),
    NavigationItem(
        title = R.string.chat,
        icon = Icons.AutoMirrored.Filled.Chat,
        section = AdminSection.CHAT
    )
)