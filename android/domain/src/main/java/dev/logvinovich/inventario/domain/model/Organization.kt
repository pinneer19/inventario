package dev.logvinovich.inventario.domain.model

data class Organization(
    val id: Long,
    val name: String,
    val adminUserId: Long
)