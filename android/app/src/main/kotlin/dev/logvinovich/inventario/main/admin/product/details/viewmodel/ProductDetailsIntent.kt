package dev.logvinovich.inventario.main.admin.product.details.viewmodel

import android.net.Uri

sealed interface ProductDetailsIntent {
    data class UpdateImageUri(val uri: Uri?) : ProductDetailsIntent

    data class UpdateName(val name: String) : ProductDetailsIntent

    data class UpdateDescription(val description: String) : ProductDetailsIntent

    data class UpdateBarcode(val barcode: String) : ProductDetailsIntent

    data object SaveProduct : ProductDetailsIntent
}