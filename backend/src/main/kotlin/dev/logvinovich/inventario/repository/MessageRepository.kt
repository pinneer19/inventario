package dev.logvinovich.inventario.repository

import dev.logvinovich.inventario.entity.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long> {
    fun findAllByOrganizationId(organizationId: Long): List<Message>
}