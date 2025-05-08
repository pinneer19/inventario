package dev.logvinovich.inventario.service.warehouse

import dev.logvinovich.inventario.entity.Role
import dev.logvinovich.inventario.entity.User
import dev.logvinovich.inventario.entity.Warehouse
import dev.logvinovich.inventario.model.AssignManagerRequest
import dev.logvinovich.inventario.model.ServiceResult
import dev.logvinovich.inventario.model.UnassignManagerRequest
import dev.logvinovich.inventario.model.WarehouseDto
import dev.logvinovich.inventario.model.toDto
import dev.logvinovich.inventario.repository.WarehouseRepository
import dev.logvinovich.inventario.service.organization.OrganizationService
import dev.logvinovich.inventario.service.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class WarehouseServiceImpl(
    private val warehouseRepository: WarehouseRepository,
    private val organizationService: OrganizationService,
    private val userService: UserService
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
        return warehouseRepository.findById(warehouseId).orElse(null)
    }

    override fun updateWarehouse(warehouseDto: WarehouseDto): ServiceResult<Warehouse> {
        val warehouse = warehouseRepository.findById(requireNotNull(warehouseDto.id)).orElse(null)
            ?: return ServiceResult.NotFound

        val updatedWarehouse = warehouseRepository.save(
            warehouse.copy(
                name = warehouseDto.name,
                managers = warehouseDto.managers.toMutableList()
            )
        )

        return ServiceResult.Success(updatedWarehouse)
    }

    override fun deleteWarehouseById(warehouseId: Long): ServiceResult<Unit> {
        if (warehouseRepository.findById(warehouseId).isEmpty) {
            return ServiceResult.NotFound
        }
        warehouseRepository.deleteById(warehouseId)
        return ServiceResult.Success(Unit)
    }

    override fun assignManagerToWarehouse(request: AssignManagerRequest): ServiceResult<User> {
        val user = userService.findByUsername(request.managerUsername)
            ?: return ServiceResult.NotFound

        if (user.role != Role.MANAGER || user.warehouse != null) {
            return ServiceResult.BadRequest
        }

        val warehouse = warehouseRepository.findById(request.warehouseId).orElse(null)
            ?: return ServiceResult.NotFound

        warehouse.assignManager(user)
        warehouseRepository.save(warehouse)

        return ServiceResult.Success(user)
    }

    override fun unassignManagerFromWarehouse(request: UnassignManagerRequest): ServiceResult<User> {
        val user = userService.getUserById(request.managerId)
            ?: return ServiceResult.NotFound

        val warehouse = warehouseRepository.findById(request.warehouseId).orElse(null)
            ?: return ServiceResult.NotFound

        if (user.role != Role.MANAGER || user.warehouse != warehouse) {
            return ServiceResult.BadRequest
        }

        warehouse.unassignManager(user)
        warehouseRepository.save(warehouse)

        return ServiceResult.Success(user)
    }
}