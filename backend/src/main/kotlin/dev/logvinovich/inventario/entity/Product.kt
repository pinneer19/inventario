package dev.logvinovich.inventario.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @Column(name = "description")
    val description: String?,

    @Column(name = "barcode", nullable = false)
    val barcode: String,

    @Column(name = "imageUrl", unique = true)
    val imageUrl: String?,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_id")
    val organization: Organization
)