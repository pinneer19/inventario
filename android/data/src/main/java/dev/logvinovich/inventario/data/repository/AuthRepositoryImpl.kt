package dev.logvinovich.inventario.data.repository

import dev.logvinovich.inventario.data.JwtManager
import dev.logvinovich.inventario.data.api.googleAuth
import dev.logvinovich.inventario.data.api.login
import dev.logvinovich.inventario.data.api.register
import dev.logvinovich.inventario.domain.model.AuthToken
import dev.logvinovich.inventario.domain.model.Role
import dev.logvinovich.inventario.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val jwtManager: JwtManager
    ) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<AuthToken> {
        return httpClient.login(username, password).map { it.toAuthToken() }
    }

    override suspend fun register(
        username: String,
        password: String,
        role: Role
    ): Result<AuthToken> {
        return httpClient.register(username, password, role).map { it.toAuthToken() }
    }

    override suspend fun googleAuth(token: String): Result<AuthToken> {
        return httpClient.googleAuth(token).map { it.toAuthToken() }
    }

    override suspend fun logout() {
        jwtManager.clearTokens()
    }
}