package dev.logvinovich.inventario.main.warehouse.item.viewmodel

import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.domain.model.Product

data class InventoryItemUiState(
    val loading: Boolean = false,
    val isEdit: Boolean = false,
    val organizationProducts: List<Product> = emptyList(),
    val selectedProductIndex: Int? = null,
    val inventoryItemId: Long? = null,
    val quantity: String = "",
    val quantityError: Boolean = false,
    val price: String = "",
    val priceError: Boolean = false,
    val saveResult: InventoryItem? = null
)

internal fun InventoryItem.toItemUiState() = InventoryItemUiState(
    isEdit = true,
    inventoryItemId = id,
    quantity = quantity.toString(),
    price = price.toString(),
)