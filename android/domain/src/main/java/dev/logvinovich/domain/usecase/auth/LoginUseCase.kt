package dev.logvinovich.domain.usecase.auth

import dev.logvinovich.domain.model.AuthToken
import dev.logvinovich.domain.repository.AuthRepository
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<AuthToken> {
        return authRepository.login(username, password)
    }
}