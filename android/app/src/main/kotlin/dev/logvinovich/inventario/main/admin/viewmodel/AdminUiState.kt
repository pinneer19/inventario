package dev.logvinovich.inventario.main.admin.viewmodel

import dev.logvinovich.inventario.domain.model.Organization
import dev.logvinovich.inventario.domain.model.Warehouse
import dev.logvinovich.inventario.main.admin.navigation.AdminSection
import dev.logvinovich.inventario.ui.util.UiText

enum class ConfirmDialogType { UNASSIGN_MANAGER, DELETE_WAREHOUSE, NONE }

data class AdminUiState(
    val loading: Boolean = false,
    val uiMessage: UiText? = null,
    val confirmationDialogType: ConfirmDialogType = ConfirmDialogType.NONE,
    val showConfirmationDialog: Boolean = false,
    val currentSection: AdminSection = AdminSection.WAREHOUSES,
    val selectedNavigationIndex: Int = 0,
    // Organization
    val organizations: List<Organization> = emptyList(),
    val selectedOrganizationId: Long? = null,
    val organizationWarehouses: List<Warehouse> = emptyList(),
    val organizationName: String = "",
    val organizationNameHasError: Boolean = false,
    val showOrganizationDialog: Boolean = false,
    // Warehouse
    val dialogWarehouseName: String = "",
    val dialogWarehouseNameHasError: Boolean = false,
    val showWarehouseDialog: Boolean = false,
    val selectedWarehouseId: Long? = null,
    // Manager
    val dialogManagerUsername: String = "",
    val dialogManagerUsernameHasError: Boolean = false,
    val showAssignDialog: Boolean = false,
    val selectedManagerId: Long? = null
)
