package dev.logvinovich.inventario.controller

import dev.logvinovich.inventario.entity.Organization
import dev.logvinovich.inventario.entity.User
import dev.logvinovich.inventario.model.OrganizationDto
import dev.logvinovich.inventario.model.toDto
import dev.logvinovich.inventario.service.organization.OrganizationService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
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
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val organization = Organization(name = organizationName, adminUser = user)
        val result = organizationService.createOrganization(organization)
        return ResponseEntity.ok(result.toDto())
    }

    @GetMapping
    fun getAdminUserOrganizations(): ResponseEntity<List<OrganizationDto>> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val organizations = organizationService.getOrganizationsByAdminUserId(
            requireNotNull(user.id)
        ).map {
            it.toDto()
        }

        return ResponseEntity.ok(organizations)
    }
}