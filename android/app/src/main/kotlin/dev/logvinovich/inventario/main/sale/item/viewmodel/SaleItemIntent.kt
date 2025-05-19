package dev.logvinovich.inventario.main.sale.item.viewmodel

import dev.logvinovich.inventario.domain.model.SaleItem

sealed interface SaleItemIntent {
    data class SelectInventoryItem(val index: Int) : SaleItemIntent

    data class UpdateQuantity(val value: String) : SaleItemIntent

    data class UpdatePrice(val value: String) : SaleItemIntent

    data class RemoveItem(val item: SaleItem) : SaleItemIntent

    object AddItemToSale : SaleItemIntent

    object SubmitSale : SaleItemIntent
}