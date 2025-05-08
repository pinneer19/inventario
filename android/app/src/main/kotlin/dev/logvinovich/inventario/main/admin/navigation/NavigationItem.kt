package dev.logvinovich.inventario.main.admin.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.ui.graphics.vector.ImageVector

enum class AdminSection {
    WAREHOUSES, PRODUCTS, CHAT
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val section: AdminSection
)

val adminNavigationItems = listOf(
    NavigationItem(
        title = "Warehouses",
        icon = Icons.Default.Warehouse,
        section = AdminSection.WAREHOUSES
    ),
    NavigationItem(
        title = "Products",
        icon = Icons.Default.Inventory,
        section = AdminSection.PRODUCTS
    ),
    NavigationItem(
        title = "Chat",
        icon = Icons.AutoMirrored.Filled.Chat,
        section = AdminSection.CHAT
    )
)