package dev.logvinovich.inventario.main.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.inventario.domain.model.Organization
import dev.logvinovich.inventario.domain.model.User
import dev.logvinovich.inventario.domain.model.Warehouse
import dev.logvinovich.inventario.domain.usecase.auth.LogoutUseCase
import dev.logvinovich.inventario.domain.usecase.organization.CreateOrganizationUseCase
import dev.logvinovich.inventario.domain.usecase.organization.GetAdminOrganizationsUseCase
import dev.logvinovich.inventario.domain.usecase.warehouse.AssignManagerUseCase
import dev.logvinovich.inventario.domain.usecase.warehouse.CreateWarehouseUseCase
import dev.logvinovich.inventario.domain.usecase.warehouse.DeleteWarehouseUseCase
import dev.logvinovich.inventario.domain.usecase.warehouse.GetOrganizationWarehousesUseCase
import dev.logvinovich.inventario.domain.usecase.warehouse.UnassignManagerUseCase
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.main.admin.navigation.AdminSection
import dev.logvinovich.inventario.ui.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val getAdminOrganizationsUseCase: GetAdminOrganizationsUseCase,
    private val createOrganizationUseCase: CreateOrganizationUseCase,
    private val getOrganizationWarehousesUseCase: GetOrganizationWarehousesUseCase,
    private val createWarehouseUseCase: CreateWarehouseUseCase,
    private val assignManagerUseCase: AssignManagerUseCase,
    private val unassignManagerUseCase: UnassignManagerUseCase,
    private val deleteWarehouseUseCase: DeleteWarehouseUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private var _uiState = MutableStateFlow(AdminUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAdminData()
    }

    fun handleIntent(intent: AdminIntent) {
        when (intent) {
            AdminIntent.ClearUiMessage -> clearUiMessage()
            AdminIntent.Logout -> logout()
            is AdminIntent.UpdateLoading -> updateLoading(intent.loading)
            is AdminIntent.ChangeSection -> changeSection(intent.section, intent.index)

            AdminIntent.CreateOrganization -> createOrganization()
            AdminIntent.ShowOrganizationDialog -> updateOrganizationDialogVisibility(visible = true)
            AdminIntent.DismissOrganizationDialog -> updateOrganizationDialogVisibility(visible = false)
            AdminIntent.ClearOrganizationName -> updateOrganizationName(organizationName = "")
            is AdminIntent.UpdateOrganizationName -> updateOrganizationName(intent.name)
            is AdminIntent.SelectOrganization -> getOrganizationWarehouses(intent.organizationId)

            AdminIntent.CreateWarehouse -> createWarehouse()
            AdminIntent.ShowWarehouseDialog -> updateWarehouseDialogVisibility(visible = true)
            AdminIntent.DismissWarehouseDialog -> updateWarehouseDialogVisibility(visible = false)
            AdminIntent.DeleteWarehouse -> deleteWarehouse()
            is AdminIntent.UpdateWarehouseName -> updateWarehouseName(intent.name)

            AdminIntent.AssignManager -> assignManagerToWarehouse()
            AdminIntent.UnassignManager -> unassignManagerFromWarehouse()
            AdminIntent.DismissConfirmationDialog -> updateConfirmationDialogState(visible = false)
            AdminIntent.DismissAssignDialog -> updateAssignDialogVisibility(visible = false)
            is AdminIntent.UpdateManagerUsername -> updateManagerUsername(intent.username)
            is AdminIntent.ShowAssignDialog -> updateAssignDialogVisibility(
                visible = true,
                warehouseId = intent.warehouseId
            )

            is AdminIntent.ShowConfirmationDialog -> updateConfirmationDialogState(
                visible = true,
                managerId = intent.managerId,
                warehouseId = intent.warehouseId,
                dialogType = intent.dialogType
            )
        }
    }

    private fun getAdminData() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result: Result<List<Organization>> = getAdminOrganizationsUseCase()

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        organizations = result.getOrThrow(),
                        selectedOrganizationId = result.getOrThrow().firstOrNull()?.id,
                        loading = false
                    )
                } else {
                    it.copy(
                        uiMessage = UiText.Plain(result.exceptionOrNull()?.message.orEmpty()),
                        loading = false
                    )
                }
            }

            _uiState.value.selectedOrganizationId?.let {
                getOrganizationWarehouses(organizationId = it)
            }
        }
    }

    private fun clearUiMessage() {
        _uiState.update { it.copy(uiMessage = null) }
    }

    private fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }

    private fun updateLoading(loading: Boolean) {
        _uiState.update { it.copy(loading = loading) }
    }

    private fun changeSection(section: AdminSection, index: Int) {
        _uiState.update {
            it.copy(
                currentSection = section,
                selectedNavigationIndex = index
            )
        }
    }

    private fun createOrganization() {
        if (_uiState.value.organizationName.isEmpty()) {
            _uiState.update { it.copy(organizationNameHasError = true) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result: Result<Organization> = createOrganizationUseCase(
                _uiState.value.organizationName
            )

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        organizations = it.organizations + result.getOrThrow(),
                        loading = false,
                        organizationName = ""
                    )
                } else {
                    it.copy(
                        uiMessage = UiText.Resource(R.string.create_organization_fail),
                        loading = false
                    )
                }
            }
        }
    }

    private fun createWarehouse() {
        if (_uiState.value.dialogWarehouseName.isEmpty()) {
            _uiState.update { it.copy(dialogWarehouseNameHasError = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result: Result<Warehouse> = createWarehouseUseCase(
                _uiState.value.dialogWarehouseName,
                requireNotNull(_uiState.value.selectedOrganizationId)
            )

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        organizationWarehouses = it.organizationWarehouses + result.getOrThrow(),
                        loading = false
                    )
                } else {
                    it.copy(
                        uiMessage = UiText.Plain(result.exceptionOrNull()?.message.orEmpty()),
                        loading = false
                    )
                }
            }
        }
    }

    private fun updateOrganizationName(organizationName: String) {
        _uiState.update {
            it.copy(organizationName = organizationName)
        }
    }

    private fun updateAssignDialogVisibility(visible: Boolean, warehouseId: Long? = null) {
        _uiState.update {
            it.copy(showAssignDialog = visible, selectedWarehouseId = warehouseId)
        }
    }

    private fun updateManagerUsername(username: String) {
        _uiState.update {
            it.copy(dialogManagerUsername = username)
        }
    }

    private fun updateWarehouseDialogVisibility(visible: Boolean) {
        _uiState.update { it.copy(showWarehouseDialog = visible) }
    }

    private fun updateOrganizationDialogVisibility(visible: Boolean) {
        _uiState.update { it.copy(showOrganizationDialog = visible) }
    }

    private fun updateConfirmationDialogState(
        visible: Boolean,
        dialogType: ConfirmDialogType = ConfirmDialogType.NONE,
        managerId: Long? = null,
        warehouseId: Long? = null
    ) {
        _uiState.update {
            it.copy(
                confirmationDialogType = dialogType,
                showConfirmationDialog = visible,
                selectedManagerId = managerId,
                selectedWarehouseId = warehouseId
            )
        }
    }

    private fun assignManagerToWarehouse() {
        with(_uiState.value) {
            if (dialogManagerUsername.isEmpty()) {
                _uiState.update { it.copy(dialogManagerUsernameHasError = true) }
                return
            }

            val selectedWarehouse = organizationWarehouses.first { warehouse ->
                warehouse.id == selectedWarehouseId
            }
            val isAlreadyAssigned = selectedWarehouse.managers.any { manager ->
                manager.username == dialogManagerUsername
            }

            if (isAlreadyAssigned) {
                _uiState.update {
                    it.copy(uiMessage = UiText.Resource(R.string.manager_already_assigned))
                }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, showAssignDialog = false) }
            val result: Result<User> = with(_uiState.value) {
                assignManagerUseCase(dialogManagerUsername, requireNotNull(selectedWarehouseId))
            }

            _uiState.update { state ->
                if (result.isSuccess) {
                    val updatedWarehouses = state.organizationWarehouses.map { warehouse ->
                        if (warehouse.id == state.selectedWarehouseId) {
                            warehouse.copy(managers = warehouse.managers + result.getOrThrow())
                        } else warehouse
                    }
                    state.copy(
                        loading = false,
                        uiMessage = UiText.Resource(R.string.manager_was_assigned),
                        dialogManagerUsername = "",
                        selectedWarehouseId = null,
                        organizationWarehouses = updatedWarehouses
                    )
                } else {
                    state.copy(
                        loading = false,
                        uiMessage = UiText.Resource(R.string.manager_not_found),
                        dialogManagerUsernameHasError = true
                    )
                }
            }
        }
    }

    private fun unassignManagerFromWarehouse() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, showConfirmationDialog = false) }
            val result: Result<User> = with(_uiState.value) {
                unassignManagerUseCase(
                    managerId = requireNotNull(selectedManagerId),
                    warehouseId = requireNotNull(selectedWarehouseId)
                )
            }

            _uiState.update { state ->
                if (result.isSuccess) {
                    val updatedWarehouses = state.organizationWarehouses.map { warehouse ->
                        if (warehouse.id == state.selectedWarehouseId) {
                            warehouse.copy(managers = warehouse.managers - result.getOrThrow())
                        } else warehouse
                    }
                    state.copy(
                        loading = false,
                        uiMessage = UiText.Resource(R.string.manager_was_unassigned),
                        selectedWarehouseId = null,
                        selectedManagerId = null,
                        organizationWarehouses = updatedWarehouses
                    )
                } else {
                    state.copy(
                        uiMessage = UiText.Plain(result.exceptionOrNull()?.message.orEmpty()),
                        loading = false,
                        selectedWarehouseId = null,
                        selectedManagerId = null
                    )
                }
            }
        }
    }

    private fun getOrganizationWarehouses(organizationId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, selectedOrganizationId = organizationId) }
            val result: Result<List<Warehouse>> = getOrganizationWarehousesUseCase(organizationId)

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(organizationWarehouses = result.getOrThrow(), loading = false)
                } else {
                    it.copy(
                        uiMessage = UiText.Plain(result.exceptionOrNull()?.message.orEmpty()),
                        loading = false
                    )
                }
            }
        }
    }

    private fun updateWarehouseName(warehouseName: String) {
        _uiState.update {
            it.copy(dialogWarehouseName = warehouseName)
        }
    }

    private fun deleteWarehouse() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, showConfirmationDialog = false) }
            val result: Result<Unit> = deleteWarehouseUseCase(
                requireNotNull(_uiState.value.selectedWarehouseId)
            )

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        organizationWarehouses = it.organizationWarehouses.filterNot { warehouse ->
                            warehouse.id == it.selectedWarehouseId
                        },
                        loading = false,
                        selectedWarehouseId = null
                    )
                } else {
                    it.copy(
                        uiMessage = UiText.Plain(result.exceptionOrNull()?.message.orEmpty()),
                        loading = false,
                        selectedWarehouseId = null
                    )
                }
            }
        }
    }
}