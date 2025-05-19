package dev.logvinovich.inventario.service.warehouse

import dev.logvinovich.inventario.entity.Role
import dev.logvinovich.inventario.entity.User
import dev.logvinovich.inventario.entity.Warehouse
import dev.logvinovich.inventario.model.AssignManagerRequest
import dev.logvinovich.inventario.model.ServiceResult
import dev.logvinovich.inventario.model.UnassignManagerRequest
import dev.logvinovich.inventario.model.WarehouseDto
import dev.logvinovich.inventario.repository.WarehouseRepository
import dev.logvinovich.inventario.security.CurrentUserProvider
import dev.logvinovich.inventario.service.organization.OrganizationService
import dev.logvinovich.inventario.service.user.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class WarehouseServiceImpl(
    private val warehouseRepository: WarehouseRepository,
    private val organizationService: OrganizationService,
    private val userService: UserService,
    private val currentUserProvider: CurrentUserProvider
) : WarehouseService {
    override fun createWarehouse(warehouseDto: WarehouseDto): Warehouse? {
        val organization = organizationService.getOrganizationById(warehouseDto.organizationId) ?: return null
        val warehouse = Warehouse(name = warehouseDto.name, organization = organization)
        return warehouseRepository.save(warehouse)
    }

    override fun getWarehousesByOrganizationId(organizationId: Long): List<Warehouse> {
        return warehouseRepository.getWarehousesByOrganizationId(organizationId)
    }

    override fun findById(warehouseId: Long): Warehouse? {
        return warehouseRepository.findByIdOrNull(warehouseId)
    }

    override fun updateWarehouse(warehouseDto: WarehouseDto): ServiceResult<Warehouse> {
        val warehouse = warehouseRepository.findByIdOrNull(requireNotNull(warehouseDto.id))
            ?: return ServiceResult.NotFound

        val updatedWarehouse = warehouseRepository.save(
            warehouse.copy(
                name = warehouseDto.name,
                managers = warehouseDto.managers.toMutableSet()
            )
        )

        return ServiceResult.Success(updatedWarehouse)
    }

    override fun deleteWarehouseById(warehouseId: Long): ServiceResult<Unit> {
        val warehouse = warehouseRepository.findById(warehouseId).getOrElse { return ServiceResult.NotFound }

        warehouse.managers.forEach { manager -> manager.warehouses.remove(warehouse) }
        warehouse.managers.clear()
        warehouseRepository.delete(warehouse)

        return ServiceResult.Success(Unit)
    }

    override fun assignManagerToWarehouse(request: AssignManagerRequest): ServiceResult<User> {
        val user = userService.findByUsername(request.managerUsername)
            ?: return ServiceResult.NotFound

        if (user.role != Role.MANAGER) {
            return ServiceResult.BadRequest
        }

        val warehouse = warehouseRepository.findByIdOrNull(request.warehouseId)
            ?: return ServiceResult.NotFound

        warehouse.assignManager(user)
        warehouseRepository.save(warehouse)

        return ServiceResult.Success(user)
    }

    override fun unassignManagerFromWarehouse(request: UnassignManagerRequest): ServiceResult<User> {
        val user = userService.getUserById(request.managerId)
            ?: return ServiceResult.NotFound

        val warehouse = warehouseRepository.findByIdOrNull(request.warehouseId)
            ?: return ServiceResult.NotFound

        if (user.role != Role.MANAGER) {
            return ServiceResult.BadRequest
        }

        if (!warehouse.managers.contains(user)) {
            return ServiceResult.BadRequest
        }

        warehouse.unassignManager(user)
        warehouseRepository.save(warehouse)

        return ServiceResult.Success(user)
    }

    override fun getWarehousesByManagerId(managerId: Long): ServiceResult<List<Warehouse>> {
        val user = userService.getUserById(managerId)
            ?: return ServiceResult.NotFound

        if (user.role != Role.MANAGER) {
            return ServiceResult.BadRequest
        }

        val warehouses = warehouseRepository.findAllByManagers_Id(managerId)
        return ServiceResult.Success(warehouses)
    }

    override fun getWarehouses(): List<Warehouse> {
        val currentUser = currentUserProvider.getCurrentUser()

        return if (currentUser.role == Role.MANAGER) {
            warehouseRepository.findAllByManagers_Id(requireNotNull(currentUser.id))
        } else {
            organizationService.getCurrentAdminOrganizations().flatMap { organization ->
                warehouseRepository.getWarehousesByOrganizationId(requireNotNull(organization.id))
            }
        }
    }
}