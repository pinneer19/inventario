package dev.logvinovich.inventario.service.organization

import dev.logvinovich.inventario.entity.Organization

interface OrganizationService {
    fun createOrganization(organization: Organization): Organization

    fun getOrganizationsByAdminUserId(adminUserId: Long): List<Organization>

    fun getOrganizationById(organizationId: Long): Organization?
}