package dev.logvinovich.inventario.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
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

    @JsonIgnore
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "warehouse_managers",
        joinColumns = [JoinColumn(name = "warehouse_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val managers: MutableSet<User> = mutableSetOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "warehouse", cascade = [CascadeType.ALL], orphanRemoval = true)
    val inventoryItems: List<InventoryItem> = emptyList()
) {
    fun assignManager(manager: User) {
        managers.add(manager)
        manager.warehouses.add(this)
    }

    fun unassignManager(manager: User) {
        managers.remove(manager)
        manager.warehouses.remove(this)
    }

    override fun equals(other: Any?) = other is Warehouse && id == other.id

    override fun hashCode() = id?.hashCode() ?: 0
}