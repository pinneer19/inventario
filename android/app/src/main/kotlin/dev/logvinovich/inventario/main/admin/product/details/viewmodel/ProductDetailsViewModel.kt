package dev.logvinovich.inventario.main.admin.product.details.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.domain.model.Product
import dev.logvinovich.domain.usecase.product.SaveProductUseCase
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.ui.util.SnackbarController
import dev.logvinovich.inventario.util.ImageUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ProductDetailsViewModel.Factory::class)
class ProductDetailsViewModel @AssistedInject constructor(
    @Assisted private val product: Product?,
    @Assisted private val organizationId: Long,
    private val imageUtil: ImageUtil,
    private val saveProductUseCase: SaveProductUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        product?.toDetailsUiState() ?: ProductDetailsUiState()
    )
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: ProductDetailsIntent) {
        when (intent) {
            is ProductDetailsIntent.UpdateImageUri -> updateImageUri(intent.uri)

            is ProductDetailsIntent.UpdateName -> updateName(intent.name)

            is ProductDetailsIntent.UpdateDescription -> updateDescription(intent.description)

            is ProductDetailsIntent.UpdateBarcode -> updateBarcode(intent.barcode)

            ProductDetailsIntent.SaveProduct -> saveProduct()
        }
    }

    private fun updateImageUri(uri: Uri?) {
        uri?.let {
            _uiState.update { it.copy(image = uri) }
        }
    }

    private fun updateName(name: String) {
        _uiState.update {
            it.copy(name = name)
        }
    }

    private fun updateDescription(description: String) {
        _uiState.update {
            it.copy(description = description)
        }
    }

    private fun updateBarcode(barcode: String) {
        _uiState.update {
            it.copy(barcode = barcode)
        }
    }

    private fun saveProduct() {
        if (_uiState.value.name.isBlank()) {
            _uiState.update { it.copy(nameError = true) }
            return
        }

        if (_uiState.value.barcode.isBlank()) {
            _uiState.update { it.copy(barcodeError = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            with(_uiState.value) {
                val imageBytes = (image as? Uri)?.let { imageUtil.compressImage(it) }
                val result = saveProductUseCase(
                    productId,
                    name,
                    description,
                    barcode,
                    organizationId,
                    imageBytes
                )

                if (result.isSuccess) {
                    _uiState.update {
                        it.copy(loading = false, saveResult = result.getOrThrow())
                    }
                    SnackbarController.sendMessageResEvent(R.string.product_saved)
                } else {
                    _uiState.update { it.copy(loading = false) }
                    SnackbarController.sendMessageResEvent(R.string.product_save_failed)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(product: Product?, organizationId: Long): ProductDetailsViewModel
    }
}