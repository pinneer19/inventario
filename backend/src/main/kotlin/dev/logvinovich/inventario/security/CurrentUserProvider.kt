package dev.logvinovich.inventario.security

import dev.logvinovich.inventario.entity.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class CurrentUserProvider {
    fun getCurrentUser(): User {
        return SecurityContextHolder.getContext().authentication.principal as User
    }
}