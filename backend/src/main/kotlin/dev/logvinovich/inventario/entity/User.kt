package dev.logvinovich.inventario.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @get:JvmName("getUserUsername")
    @Column(name = "username", nullable = false, unique = true)
    val username: String,

    @get:JvmName("getUserPassword")
    @Column(name = "password")
    val password: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: Role = Role.MANAGER,

    @JsonIgnore
    @ManyToMany(mappedBy = "managers")
    val warehouses: MutableSet<Warehouse> = mutableSetOf()
) : UserDetails {
    override fun getAuthorities() = listOf(role)

    override fun getPassword() = password

    override fun getUsername() = username

    override fun equals(other: Any?) = other is User && id == other.id

    override fun hashCode() = id?.hashCode() ?: 0
}