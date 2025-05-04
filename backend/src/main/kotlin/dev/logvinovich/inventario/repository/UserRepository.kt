package dev.logvinovich.inventario.repository

import dev.logvinovich.inventario.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}