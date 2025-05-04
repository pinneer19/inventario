package dev.logvinovich.domain.usecase

import dev.logvinovich.domain.model.Organization
import dev.logvinovich.domain.repository.OrganizationRepository
import jakarta.inject.Inject

class GetAdminOrganizationsUseCase @Inject constructor(
    private val organizationRepository: OrganizationRepository
) {
    suspend operator fun invoke(): Result<List<Organization>> {
       return organizationRepository.getAdminUserOrganizations()
    }
}