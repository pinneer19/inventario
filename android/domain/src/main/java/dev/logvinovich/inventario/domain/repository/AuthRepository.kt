package dev.logvinovich.inventario.domain.repository

import dev.logvinovich.inventario.domain.model.AuthToken
import dev.logvinovich.inventario.domain.model.Role

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<AuthToken>

    suspend fun register(username: String, password: String, role: Role): Result<AuthToken>

    suspend fun googleAuth(token: String): Result<AuthToken>

    suspend fun logout()
}