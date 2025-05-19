package dev.logvinovich.inventario.main.sale.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.domain.usecase.sale.DeleteSaleUseCase
import dev.logvinovich.inventario.domain.usecase.sale.GetSalesUseCase
import dev.logvinovich.inventario.domain.usecase.warehouse.GetWarehousesUseCase
import dev.logvinovich.inventario.ui.util.SnackbarController
import dev.logvinovich.inventario.util.toLocalDate
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
class SalesViewModel @Inject constructor(
    private val getWarehousesUseCase: GetWarehousesUseCase,
    private val getSalesUseCase: GetSalesUseCase,
    private val deleteSaleUseCase: DeleteSaleUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getSales()
        getWarehouses()
    }

    fun handleIntent(intent: SalesIntent) {
        when (intent) {
            SalesIntent.GetSales -> getSales()
            is SalesIntent.SetDateRangeFilter -> setDateRangeFilter(intent.dateRange)
            is SalesIntent.SetWarehouseFilter -> setWarehouseFilter(intent.warehouseId)
            is SalesIntent.DeleteSale -> deleteSale(intent.saleId)
        }
    }

    private fun getWarehouses() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result = getWarehousesUseCase()

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(loading = false, warehouses = result.getOrThrow())
                }
            } else {
                _uiState.update { it.copy(loading = false) }
                SnackbarController.sendMessageResEvent(R.string.warehouse_request_failed)
            }
        }
    }

    private fun getSales() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result = with(_uiState.value) {
                val (dateFrom, dateTo) =
                    dateRange.first.toLocalDate() to dateRange.second.toLocalDate()

                getSalesUseCase(selectedWarehouseId, dateFrom, dateTo)
            }

            if (result.isSuccess) {
                val groupedSales = result.getOrThrow().groupBy { it.date.date }
                _uiState.update {
                    it.copy(
                        groupedSales = groupedSales,
                        loading = false,
                        initialDataIsEmpty = groupedSales.isEmpty()
                    )
                }
            } else {
                _uiState.update { it.copy(loading = false) }
                SnackbarController.sendMessageResEvent(R.string.sales_request_failed)
            }

        }
    }

    private fun setDateRangeFilter(range: Pair<Long?, Long?>) {
        _uiState.update { it.copy(dateRange = range) }
    }

    private fun setWarehouseFilter(warehouseId: Long?) {
        _uiState.update { it.copy(selectedWarehouseId = warehouseId) }
    }

    private fun deleteSale(saleId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result = deleteSaleUseCase(saleId)

            if (result.isSuccess) {
                val updatedSales = _uiState.value.groupedSales
                    .mapValues { entry ->
                        entry.value.filterNot { sale -> sale.id == saleId }
                    }
                    .filterNot { entry -> entry.value.isEmpty() }

                _uiState.update { it.copy(loading = false, groupedSales = updatedSales) }
                SnackbarController.sendMessageResEvent(R.string.sale_delete_success)
            } else {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }
}