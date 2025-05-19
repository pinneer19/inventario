package dev.logvinovich.inventario.main.manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.inventario.domain.usecase.auth.LogoutUseCase
import dev.logvinovich.inventario.domain.usecase.warehouse.GetManagerWarehousesUseCase
import dev.logvinovich.inventario.ui.util.SnackbarController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ManagerViewModel.Factory::class)
class ManagerViewModel @AssistedInject constructor(
    @Assisted private val managerId: Long,
    private val getManagerWarehousesUseCase: GetManagerWarehousesUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ManagerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getManagerWarehouses(managerId)
    }

    fun handleIntent(intent: ManagerIntent) {
        when (intent) {
            is ManagerIntent.UpdateSelectedWarehouse -> updateSelectedWarehouse(intent.warehouseId)
            ManagerIntent.GetWarehouses -> getManagerWarehouses(managerId)
            ManagerIntent.Logout -> logout()
        }
    }

    private fun updateSelectedWarehouse(warehouseId: Long) {
        _uiState.update {
            it.copy(selectedWarehouseId = warehouseId)
        }
    }

    private fun getManagerWarehouses(managerId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result = getManagerWarehousesUseCase(managerId)

            if (result.isSuccess) {
                val warehouses = result.getOrThrow()
                _uiState.update {
                    it.copy(
                        loading = false,
                        warehouses = warehouses,
                        selectedWarehouseId = warehouses.firstOrNull()?.id
                    )
                }
            } else {
                _uiState.update { it.copy(loading = false) }
                SnackbarController.sendMessageEvent(result.exceptionOrNull()?.message ?: "error")
            }
        }
    }

    private fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }

    @AssistedFactory
    interface Factory {
        fun create(managerId: Long): ManagerViewModel
    }
}