package dev.logvinovich.inventario.entity

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    MANAGER,
    ADMIN;

    override fun getAuthority(): String = "ROLE_$name"
}
