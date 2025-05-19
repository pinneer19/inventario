package dev.logvinovich.inventario.domain.usecase.auth

import dev.logvinovich.inventario.domain.model.AuthToken
import dev.logvinovich.inventario.domain.model.Role
import dev.logvinovich.inventario.domain.repository.AuthRepository
import jakarta.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        role: Role
    ): Result<AuthToken> {
        return authRepository.register(username, password, role)
    }
}