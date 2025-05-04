package dev.logvinovich.domain.usecase

import dev.logvinovich.domain.model.Organization
import dev.logvinovich.domain.repository.OrganizationRepository
import jakarta.inject.Inject

class CreateOrganizationUseCase @Inject constructor(
    private val organizationRepository: OrganizationRepository
) {
    suspend operator fun invoke(organizationName: String): Result<Organization> {
        return organizationRepository.createOrganization(organizationName)
    }
}
