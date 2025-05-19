package dev.logvinovich.inventario.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "sale")
data class Sale(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    val manager: User,

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    val warehouse: Warehouse,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "date", nullable = false)
    val date: LocalDateTime = LocalDateTime.now(),

    @Column(name = "total_amount", nullable = false)
    val totalAmount: Float,

    @OneToMany(mappedBy = "sale", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableSet<SaleItem> = mutableSetOf()
) {
    override fun equals(other: Any?) = other is Sale && id == other.id

    override fun hashCode() = id?.hashCode() ?: 0
}