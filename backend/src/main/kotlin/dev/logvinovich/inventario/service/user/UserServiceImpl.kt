package dev.logvinovich.inventario.service.user

import dev.logvinovich.inventario.repository.UserRepository
import dev.logvinovich.inventario.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : UserService {

    override fun findByUsername(username: String): User? = userRepository.findByUsername(username)

    override fun getUser(user: User): User? {
        val dbUser = userRepository.findByUsername(user.username) ?: return null
        return if (passwordEncoder.matches(user.password, dbUser.password)) dbUser else null
    }

    override fun insertIntoDatabase(user: User): User {
        val dbUser = user.copy(password = passwordEncoder.encode(user.password))
        return userRepository.save(dbUser)
    }

    override fun getUserById(id: Long): User? {
        return userRepository.findByIdOrNull(id)
    }

    override fun findOrInsert(user: User): User {
        return userRepository.findByUsername(user.username) ?: userRepository.save(user)
    }

    override fun updateUser(user: User): User {
        return userRepository.save(user)
    }
}