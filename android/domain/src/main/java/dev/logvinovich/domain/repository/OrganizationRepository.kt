package dev.logvinovich.domain.repository

import dev.logvinovich.domain.model.Organization

interface OrganizationRepository {
    suspend fun getAdminUserOrganizations(): Result<List<Organization>>

    suspend fun createOrganization(organizationName: String): Result<Organization>
}