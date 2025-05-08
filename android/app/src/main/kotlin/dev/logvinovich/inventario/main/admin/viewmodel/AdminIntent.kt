package dev.logvinovich.inventario.main.admin.viewmodel

sealed interface AdminIntent {
    data object ClearUiMessage : AdminIntent
    data class UpdateLoading(val loading: Boolean) : AdminIntent

    // Organization
    data object CreateOrganization : AdminIntent
    data object ShowOrganizationDialog : AdminIntent
    data object DismissOrganizationDialog : AdminIntent
    data object ClearOrganizationName : AdminIntent
    data class UpdateOrganizationName(val name: String) : AdminIntent
    data class SelectOrganization(val organizationId: Long) : AdminIntent

    // Warehouse
    data object CreateWarehouse : AdminIntent
    data object ShowWarehouseDialog : AdminIntent
    data object DismissWarehouseDialog : AdminIntent
    data object DeleteWarehouse : AdminIntent
    data class UpdateWarehouseName(val name: String) : AdminIntent

    // Manager
    data object AssignManager : AdminIntent
    data object UnassignManager : AdminIntent
    data object DismissAssignDialog : AdminIntent
    data object DismissConfirmationDialog : AdminIntent

    data class UpdateManagerUsername(val username: String) : AdminIntent
    data class ShowAssignDialog(val warehouseId: Long) : AdminIntent
    data class ShowConfirmationDialog(
        val managerId: Long? = null,
        val warehouseId: Long? = null,
        val dialogType: ConfirmDialogType
    ) : AdminIntent
}