package dev.logvinovich.inventario.service.user

import dev.logvinovich.inventario.entity.User
import org.springframework.stereotype.Component

@Component
interface UserService {
    fun findByUsername(username: String): User?

    fun getUser(user: User): User?

    fun insertIntoDatabase(user: User): User

    fun getUserById(id: Long): User?

    fun findOrInsert(user: User): User

    fun updateUser(user: User): User
}