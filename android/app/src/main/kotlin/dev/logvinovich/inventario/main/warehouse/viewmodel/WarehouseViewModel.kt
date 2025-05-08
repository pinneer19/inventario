package dev.logvinovich.inventario.main.warehouse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.domain.usecase.warehouse.GetWarehouseProductsUseCase
import dev.logvinovich.inventario.ui.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = WarehouseViewModel.Factory::class)
class WarehouseViewModel @AssistedInject constructor(
    @Assisted private val warehouseId: Long,
    private val getWarehouseProductsUseCase: GetWarehouseProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WarehouseUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getInventoryItems()
    }

    fun handleIntent(intent: WarehouseIntent) {
        when (intent) {
            WarehouseIntent.GetInventoryItems -> getInventoryItems()
        }
    }

    private fun getInventoryItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result = getWarehouseProductsUseCase(warehouseId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(loading = false, inventoryItems = result.getOrThrow())
                }
            } else {
                _uiState.update {
                    it.copy(
                        loading = false,
                        uiMessage = UiText.Plain(result.exceptionOrNull()?.message.orEmpty()),
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(warehouseId: Long): WarehouseViewModel
    }
}