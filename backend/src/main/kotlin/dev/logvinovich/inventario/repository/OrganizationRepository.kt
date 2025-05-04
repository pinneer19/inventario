package dev.logvinovich.inventario.repository

import dev.logvinovich.inventario.entity.Organization
import org.springframework.data.jpa.repository.JpaRepository

interface OrganizationRepository: JpaRepository<Organization, Long> {
    fun findAllByAdminUserId(adminUserId: Long): List<Organization>
}