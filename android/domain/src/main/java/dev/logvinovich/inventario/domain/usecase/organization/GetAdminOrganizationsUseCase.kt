package dev.logvinovich.inventario.domain.usecase.organization

import dev.logvinovich.inventario.domain.model.Organization
import dev.logvinovich.inventario.domain.repository.OrganizationRepository
import jakarta.inject.Inject

class GetAdminOrganizationsUseCase @Inject constructor(
    private val organizationRepository: OrganizationRepository
) {
    suspend operator fun invoke(): Result<List<Organization>> {
       return organizationRepository.getAdminUserOrganizations()
    }
}