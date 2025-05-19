package dev.logvinovich.inventario.domain.usecase.organization

import dev.logvinovich.inventario.domain.model.Organization
import dev.logvinovich.inventario.domain.repository.OrganizationRepository
import jakarta.inject.Inject

class CreateOrganizationUseCase @Inject constructor(
    private val organizationRepository: OrganizationRepository
) {
    suspend operator fun invoke(organizationName: String): Result<Organization> {
        return organizationRepository.createOrganization(organizationName)
    }
}
