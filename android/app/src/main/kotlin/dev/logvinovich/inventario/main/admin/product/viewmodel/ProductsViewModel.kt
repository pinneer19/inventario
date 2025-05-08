package dev.logvinovich.inventario.main.admin.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.domain.model.Product
import dev.logvinovich.domain.usecase.product.DeleteProductUseCase
import dev.logvinovich.domain.usecase.product.GetProductsUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductsUseCase: DeleteProductUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState = _uiState.asStateFlow()

    private fun getOrganizationProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result = getProductsUseCase(requireNotNull(_uiState.value.organizationId))
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(loading = false, products = result.getOrThrow())
                }
            } else {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    fun handleIntent(intent: ProductsIntent) {
        when (intent) {
            is ProductsIntent.SetOrganizationId -> setOrganizationId(intent.organizationId)
            is ProductsIntent.DeleteProduct -> deleteProduct(intent.productId)
            is ProductsIntent.SetSavedProduct -> setSavedProduct(intent.product)
        }
    }

    private fun setOrganizationId(organizationId: Long) {
        val currentId = _uiState.value.organizationId
        if (currentId != organizationId) {
            _uiState.update { it.copy(organizationId = organizationId) }
            getOrganizationProducts()
        }
    }

    private fun deleteProduct(productId: Long) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            val result = deleteProductsUseCase(productId)

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        products = it.products.filterNot { product -> product.id == productId },
                        loading = false,
                    )
                } else {
                    it.copy(loading = false)
                }
            }
        }
    }

    private fun setSavedProduct(product: Product) {
        _uiState.update { currentState ->
            val productExists = currentState.products.any { it.id == product.id }
            if (productExists) {
                val updatedProducts = currentState.products.map {
                    if (it.id == product.id) product else it
                }
                currentState.copy(products = updatedProducts)
            } else {
                currentState.copy(products = currentState.products + product)
            }
        }
    }
}