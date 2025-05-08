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
@Table(name = "inventory_item")
data class InventoryItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    val warehouse: Warehouse,

    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: Product,

    @Column(name = "quantity", nullable = false)
    val quantity: Int,

    @Column(name = "price", nullable = false)
    val price: Float
)