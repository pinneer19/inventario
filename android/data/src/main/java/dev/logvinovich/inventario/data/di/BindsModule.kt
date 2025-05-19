package dev.logvinovich.inventario.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.logvinovich.inventario.data.repository.AuthRepositoryImpl
import dev.logvinovich.inventario.data.repository.InventoryItemRepositoryImpl
import dev.logvinovich.inventario.data.repository.OrganizationRepositoryImpl
import dev.logvinovich.inventario.data.repository.ProductRepositoryImpl
import dev.logvinovich.inventario.data.repository.SaleRepositoryImpl
import dev.logvinovich.inventario.data.repository.WarehouseRepositoryImpl
import dev.logvinovich.inventario.domain.repository.AuthRepository
import dev.logvinovich.inventario.domain.repository.InventoryItemRepository
import dev.logvinovich.inventario.domain.repository.OrganizationRepository
import dev.logvinovich.inventario.domain.repository.ProductRepository
import dev.logvinovich.inventario.domain.repository.SaleRepository
import dev.logvinovich.inventario.domain.repository.WarehouseRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class BindsModule {
    @Binds
    @Singleton
    abstract fun bindOrganizationRepository(
        organizationRepositoryImpl: OrganizationRepositoryImpl
    ): OrganizationRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindWarehouseRepository(
        warehouseRepositoryImpl: WarehouseRepositoryImpl
    ): WarehouseRepository

    @Binds
    @Singleton
    abstract fun bindInventoryItemRepository(
        inventoryItemRepositoryImpl: InventoryItemRepositoryImpl
    ): InventoryItemRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindSaleRepository(
        saleRepositoryImpl: SaleRepositoryImpl
    ): SaleRepository
}