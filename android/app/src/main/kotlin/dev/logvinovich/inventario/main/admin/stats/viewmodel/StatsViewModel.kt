package dev.logvinovich.inventario.main.admin.stats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.inventario.domain.usecase.sale.GetStatsUseCase
import dev.logvinovich.inventario.ui.util.SnackbarController
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getStatsUseCase: GetStatsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getStats()
    }

    private fun getStats() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result = getStatsUseCase()

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(stats = result.getOrThrow(), loading = false)
                }
            } else {
                _uiState.update {
                    it.copy(loading = false)
                }
                SnackbarController.sendMessageEvent("FAIL")
            }
        }
    }
}
