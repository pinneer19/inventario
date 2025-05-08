package dev.logvinovich.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.logvinovich.data.repository.AuthRepositoryImpl
import dev.logvinovich.data.repository.InventoryItemRepositoryImpl
import dev.logvinovich.data.repository.OrganizationRepositoryImpl
import dev.logvinovich.data.repository.ProductRepositoryImpl
import dev.logvinovich.data.repository.WarehouseRepositoryImpl
import dev.logvinovich.domain.repository.AuthRepository
import dev.logvinovich.domain.repository.InventoryItemRepository
import dev.logvinovich.domain.repository.OrganizationRepository
import dev.logvinovich.domain.repository.ProductRepository
import dev.logvinovich.domain.repository.WarehouseRepository
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
}