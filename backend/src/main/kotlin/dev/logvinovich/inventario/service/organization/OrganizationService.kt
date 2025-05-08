package dev.logvinovich.inventario.service.organization

import dev.logvinovich.inventario.entity.Organization
import dev.logvinovich.inventario.model.ServiceResult

interface OrganizationService {
    fun createOrganization(organizationName: String): Organization

    fun getCurrentAdminOrganizations(): List<Organization>

    fun getOrganizationById(organizationId: Long): Organization?

    fun updateOrganizationName(organizationId: Long, newName: String): ServiceResult<Organization>

    fun deleteOrganization(organizationId: Long): ServiceResult<Unit>
}