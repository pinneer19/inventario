package dev.logvinovich.inventario.service.organization

import dev.logvinovich.inventario.entity.Organization
import dev.logvinovich.inventario.model.ServiceResult
import dev.logvinovich.inventario.repository.OrganizationRepository
import dev.logvinovich.inventario.security.CurrentUserProvider
import org.springframework.stereotype.Service

@Service
class OrganizationServiceImpl(
    private val organizationRepository: OrganizationRepository,
    private val currentUserProvider: CurrentUserProvider
) : OrganizationService {
    override fun createOrganization(organizationName: String): Organization {
        val user = currentUserProvider.getCurrentUser()
        val organization = Organization(name = organizationName, adminUser = user)
        return organizationRepository.save(organization)
    }

    override fun getCurrentAdminOrganizations(): List<Organization> {
        val userId = requireNotNull(currentUserProvider.getCurrentUser().id)
        return organizationRepository.findAllByAdminUserId(userId)
    }

    override fun getOrganizationById(organizationId: Long): Organization? {
        return organizationRepository.findById(organizationId).orElse(null)
    }

    override fun updateOrganizationName(organizationId: Long, newName: String): ServiceResult<Organization> {
        val user = currentUserProvider.getCurrentUser()

        val organization = organizationRepository.findById(organizationId).orElse(null)
            ?: return ServiceResult.NotFound

        if (organization.adminUser.id != user.id) {
            return ServiceResult.Forbidden
        }

        val updatedOrganization = organization.copy(name = newName)
        organizationRepository.save(updatedOrganization)

        return ServiceResult.Success(updatedOrganization)
    }

    override fun deleteOrganization(organizationId: Long): ServiceResult<Unit> {
        val user = currentUserProvider.getCurrentUser()

        val organization = organizationRepository.findById(organizationId).orElse(null)
            ?: return ServiceResult.NotFound

        if (organization.adminUser.id != user.id) {
            return ServiceResult.Forbidden
        }

        organizationRepository.deleteById(organizationId)

        return ServiceResult.Success(Unit)
    }
}