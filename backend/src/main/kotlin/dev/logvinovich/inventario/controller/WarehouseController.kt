package dev.logvinovich.inventario.controller

import dev.logvinovich.inventario.entity.Role
import dev.logvinovich.inventario.model.AssignManagerRequest
import dev.logvinovich.inventario.model.UnassignManagerRequest
import dev.logvinovich.inventario.model.UserDto
import dev.logvinovich.inventario.model.WarehouseDto
import dev.logvinovich.inventario.model.toDto
import dev.logvinovich.inventario.service.user.UserService
import dev.logvinovich.inventario.service.warehouse.WarehouseService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/warehouses")
class WarehouseController(
    private val warehouseService: WarehouseService,
    private val userService: UserService
) {
    @PostMapping
    fun createWarehouse(
        @RequestBody warehouseDto: WarehouseDto
    ): ResponseEntity<WarehouseDto> {
        return warehouseService.createWarehouse(warehouseDto)
            ?.let { ResponseEntity.ok(it.toDto()) }
            ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping
    fun deleteWarehouse(
        @RequestParam warehouseId: Long
    ): ResponseEntity<Unit> {
        if (warehouseService.findById(warehouseId) == null) {
            return ResponseEntity.notFound().build()
        }
        warehouseService.deleteWarehouseById(warehouseId)

        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getOrganizationWarehouses(@RequestParam organizationId: Long): List<WarehouseDto> {
        return warehouseService.getWarehousesByOrganizationId(organizationId).map { it.toDto() }
    }

    @PostMapping("/assign-manager")
    fun assignManagerToWarehouse(@RequestBody request: AssignManagerRequest): ResponseEntity<UserDto> {
        val user = userService.findByUsername(request.managerUsername)
            ?: return ResponseEntity.notFound().build()

        if (user.role != Role.MANAGER) {
            return ResponseEntity.badRequest().build()
        }

        if (user.warehouse != null) {
            return ResponseEntity.badRequest().build()
        }

        val warehouse = warehouseService.findById(request.warehouseId)
            ?: return ResponseEntity.badRequest().build()

        warehouse.assignManager(user)
        warehouseService.updateWarehouse(warehouse)

        return ResponseEntity.ok(user.toDto())
    }

    @PostMapping("/unassign-manager")
    fun unassignManagerFromWarehouse(@RequestBody request: UnassignManagerRequest): ResponseEntity<UserDto> {
        val user = userService.getUserById(request.managerId)
            ?: return ResponseEntity.notFound().build()

        if (user.role != Role.MANAGER) {
            return ResponseEntity.badRequest().build()
        }

        val warehouse = warehouseService.findById(request.warehouseId)
            ?: return ResponseEntity.badRequest().build()

        if (user.warehouse != warehouse) {
            return ResponseEntity.badRequest().build()
        }

        warehouse.unassignManager(user)
        warehouseService.updateWarehouse(warehouse)

        return ResponseEntity.ok(user.toDto())
    }
}