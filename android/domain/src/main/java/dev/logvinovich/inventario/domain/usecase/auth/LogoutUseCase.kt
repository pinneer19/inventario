package dev.logvinovich.inventario.domain.usecase.auth

import dev.logvinovich.inventario.domain.repository.AuthRepository
import jakarta.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        return authRepository.logout()
    }
}