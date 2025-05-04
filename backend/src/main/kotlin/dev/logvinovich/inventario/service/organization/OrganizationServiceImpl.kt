package dev.logvinovich.inventario.service.organization

import dev.logvinovich.inventario.entity.Organization
import dev.logvinovich.inventario.repository.OrganizationRepository
import org.springframework.stereotype.Service

@Service
class OrganizationServiceImpl(
    private val organizationRepository: OrganizationRepository
) : OrganizationService {
    override fun createOrganization(organization: Organization): Organization {
        return organizationRepository.save(organization)
    }

    override fun getOrganizationsByAdminUserId(adminUserId: Long): List<Organization> {
        return organizationRepository.findAllByAdminUserId(adminUserId)
    }

    override fun getOrganizationById(organizationId: Long): Organization? {
        return organizationRepository.findById(organizationId).orElse(null)
    }
}