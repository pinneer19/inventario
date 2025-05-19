package dev.logvinovich.inventario.main.warehouse.item.viewmodel

sealed interface InventoryItemIntent {
    data class UpdateQuantity(val quantity: String) : InventoryItemIntent

    data class UpdatePrice(val price: String) : InventoryItemIntent

    data class UpdateSelectedIndex(val index: Int) : InventoryItemIntent

    data object SaveItem : InventoryItemIntent
}