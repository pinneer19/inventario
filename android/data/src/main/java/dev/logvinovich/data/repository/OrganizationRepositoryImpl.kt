package dev.logvinovich.data.repository

import dev.logvinovich.data.api.createOrganization
import dev.logvinovich.data.api.getAdminUserOrganizations
import dev.logvinovich.data.util.map
import dev.logvinovich.domain.model.Organization
import dev.logvinovich.domain.repository.OrganizationRepository
import io.ktor.client.HttpClient
import javax.inject.Inject

class OrganizationRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
): OrganizationRepository {
    override suspend fun getAdminUserOrganizations(): Result<List<Organization>> {
        return httpClient.getAdminUserOrganizations().map { dtoList ->
            dtoList.map { it.toOrganization() }
        }
    }

    override suspend fun createOrganization(organizationName: String): Result<Organization> {
        return httpClient.createOrganization(organizationName).map {
            it.toOrganization()
        }
    }
}