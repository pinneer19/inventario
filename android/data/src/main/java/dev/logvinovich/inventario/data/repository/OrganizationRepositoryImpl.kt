package dev.logvinovich.inventario.data.repository

import dev.logvinovich.inventario.data.api.createOrganization
import dev.logvinovich.inventario.data.api.getAdminUserOrganizations
import dev.logvinovich.inventario.data.util.map
import dev.logvinovich.inventario.domain.model.Organization
import dev.logvinovich.inventario.domain.repository.OrganizationRepository
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