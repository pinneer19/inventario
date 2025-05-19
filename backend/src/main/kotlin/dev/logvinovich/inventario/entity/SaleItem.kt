package dev.logvinovich.inventario.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "sale_item")
data class SaleItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    val sale: Sale,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val inventoryItem: InventoryItem,

    @Column(nullable = false)
    val quantity: Int,

    @Column(nullable = false)
    val price: Float
)