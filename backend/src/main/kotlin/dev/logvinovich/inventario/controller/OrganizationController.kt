package dev.logvinovich.inventario.controller

import dev.logvinovich.inventario.entity.Organization
import dev.logvinovich.inventario.model.OrganizationDto
import dev.logvinovich.inventario.model.toDto
import dev.logvinovich.inventario.model.toResponseEntity
import dev.logvinovich.inventario.service.organization.OrganizationService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/organizations")
class OrganizationController(
    private val organizationService: OrganizationService
) {
    @PostMapping
    fun createOrganization(@RequestParam organizationName: String): ResponseEntity<OrganizationDto> {
        val result = organizationService.createOrganization(organizationName)
        return ResponseEntity.ok(result.toDto())
    }

    @GetMapping
    fun getAdminUserOrganizations(): ResponseEntity<List<OrganizationDto>> {
        val organizations = organizationService.getCurrentAdminOrganizations()
            .map { it.toDto() }
        return ResponseEntity.ok(organizations)
    }

    @PutMapping("/{id}")
    fun updateOrganization(
        @PathVariable id: Long,
        @RequestParam organizationName: String
    ): ResponseEntity<OrganizationDto> {
        val result = organizationService.updateOrganizationName(id, organizationName)
        return result.toResponseEntity(Organization::toDto)
    }

    @DeleteMapping("/{id}")
    fun deleteOrganization(@PathVariable id: Long): ResponseEntity<Unit> {
        val result = organizationService.deleteOrganization(id)
        return result.toResponseEntity()
    }
}