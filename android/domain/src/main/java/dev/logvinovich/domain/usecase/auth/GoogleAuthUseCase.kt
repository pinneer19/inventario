package dev.logvinovich.domain.usecase.auth

import dev.logvinovich.domain.model.AuthToken
import dev.logvinovich.domain.repository.AuthRepository
import jakarta.inject.Inject

class GoogleAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(token: String): Result<AuthToken> {
        return authRepository.googleAuth(token)
    }
}