package dev.logvinovich.inventario.main.sale.item.viewmodel

import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.domain.model.SaleItem

data class SaleItemUiState(
    val saleItems: List<SaleItem> = emptyList(),
    val loading: Boolean = false,
    val saleCompleted: Boolean = false,
    val availableItems: List<InventoryItem> = emptyList(),
    val selectedItemIndex: Int? = null,
    val quantity: String = "",
    val availableQuantity: String? = null,
    val quantityError: Boolean = false,
    val price: String = "",
    val priceError: Boolean = false,
)