package dev.logvinovich.inventario.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "messages")
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    val sender: User,

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    val organization: Organization,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    val timestamp: LocalDateTime = LocalDateTime.now()
)
