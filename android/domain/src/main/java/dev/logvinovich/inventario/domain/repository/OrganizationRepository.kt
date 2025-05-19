package dev.logvinovich.inventario.domain.repository

import dev.logvinovich.inventario.domain.model.Organization

interface OrganizationRepository {
    suspend fun getAdminUserOrganizations(): Result<List<Organization>>

    suspend fun createOrganization(organizationName: String): Result<Organization>
}