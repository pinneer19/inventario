package dev.logvinovich.inventario.controller

import dev.logvinovich.inventario.entity.User
import dev.logvinovich.inventario.entity.Warehouse
import dev.logvinovich.inventario.model.AssignManagerRequest
import dev.logvinovich.inventario.model.UnassignManagerRequest
import dev.logvinovich.inventario.model.UserDto
import dev.logvinovich.inventario.model.WarehouseDto
import dev.logvinovich.inventario.model.toDto
import dev.logvinovich.inventario.model.toResponseEntity
import dev.logvinovich.inventario.service.user.UserService
import dev.logvinovich.inventario.service.warehouse.WarehouseService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

    @PutMapping("/{id}")
    fun updateWarehouse(
        @PathVariable id: Long,
        @RequestParam warehouseDto: WarehouseDto
    ): ResponseEntity<WarehouseDto> {
        val updateResult = warehouseService.updateWarehouse(warehouseDto)
        return updateResult.toResponseEntity(Warehouse::toDto)
    }


    @DeleteMapping("/{id}")
    fun deleteWarehouse(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        val deleteResult = warehouseService.deleteWarehouseById(id)
        return deleteResult.toResponseEntity()
    }

    @GetMapping
    fun getOrganizationWarehouses(@RequestParam organizationId: Long): ResponseEntity<List<WarehouseDto>> {
        return ResponseEntity.ok(
            warehouseService.getWarehousesByOrganizationId(organizationId).map { it.toDto() }
        )
    }

    @PostMapping("/assign-manager")
    fun assignManagerToWarehouse(@RequestBody request: AssignManagerRequest): ResponseEntity<UserDto> {
        val assignResult = warehouseService.assignManagerToWarehouse(request)
        return assignResult.toResponseEntity(User::toDto)
    }

    @PostMapping("/unassign-manager")
    fun unassignManagerFromWarehouse(@RequestBody request: UnassignManagerRequest): ResponseEntity<UserDto> {
        val unassignResult = warehouseService.unassignManagerFromWarehouse(request)
        return unassignResult.toResponseEntity(User::toDto)
    }
}