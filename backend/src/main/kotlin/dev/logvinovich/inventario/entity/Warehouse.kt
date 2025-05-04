package dev.logvinovich.inventario.entity

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

@Entity
@Table(name = "warehouses")
data class Warehouse(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "organization_id")
    val organization: Organization,

    @OneToMany(mappedBy = "warehouse", cascade = [CascadeType.ALL])
    val managers: MutableList<User> = mutableListOf()
) {
    fun assignManager(manager: User) {
        managers.add(manager)
        manager.warehouse = this
    }

    fun unassignManager(manager: User) {
        managers.remove(manager)
        manager.warehouse = null
    }
}