package dev.logvinovich.inventario.service.sale

import dev.logvinovich.inventario.entity.Role
import dev.logvinovich.inventario.entity.Sale
import dev.logvinovich.inventario.entity.SaleItem
import dev.logvinovich.inventario.model.SaleDto
import dev.logvinovich.inventario.model.SaleItemDto
import dev.logvinovich.inventario.model.ServiceResult
import dev.logvinovich.inventario.model.StatsDto
import dev.logvinovich.inventario.model.toDto
import dev.logvinovich.inventario.repository.InventoryItemRepository
import dev.logvinovich.inventario.repository.OrganizationRepository
import dev.logvinovich.inventario.repository.SaleItemRepository
import dev.logvinovich.inventario.repository.SaleRepository
import dev.logvinovich.inventario.repository.WarehouseRepository
import dev.logvinovich.inventario.security.CurrentUserProvider
import dev.logvinovich.inventario.service.organization.OrganizationService
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class SaleServiceImpl(
    private val saleRepository: SaleRepository,
    private val warehouseRepository: WarehouseRepository,
    private val organizationService: OrganizationService,
    private val inventoryItemRepository: InventoryItemRepository,
    private val saleItemRepository: SaleItemRepository,
    private val currentUserProvider: CurrentUserProvider
) : SaleService {

    @Transactional
    override fun createSale(warehouseId: Long, items: List<SaleItemDto>): SaleDto? {
        val currentUser = currentUserProvider.getCurrentUser()

        if (currentUser.role != Role.MANAGER) return null

        val warehouse = warehouseRepository.findByIdOrNull(warehouseId) ?: return null

        val sale = Sale(
            manager = currentUser,
            warehouse = warehouse,
            date = LocalDateTime.now(),
            totalAmount = items.sumOf { it.quantity * it.price.toDouble() }.toFloat()
        )

        val savedSale = saleRepository.save(sale)

        val saleItems = items.mapNotNull { itemDto ->
            val inventoryItem = inventoryItemRepository.findByIdOrNull(itemDto.inventoryItemId)
                ?: return@mapNotNull null

            if (inventoryItem.quantity < itemDto.quantity) {
                throw IllegalArgumentException("Not enough stock for inventory item ${itemDto.inventoryItemId}")
            }

            inventoryItemRepository.save(
                inventoryItem.copy(quantity = inventoryItem.quantity - itemDto.quantity)
            )

            SaleItem(
                sale = savedSale,
                inventoryItem = inventoryItem,
                quantity = itemDto.quantity,
                price = itemDto.price
            )
        }.toMutableSet()

        val finalSale = savedSale.copy(items = saleItems)
        return saleRepository.save(finalSale).toDto()
    }

    override fun getSales(
        warehouseId: Long?,
        fromDate: LocalDate?,
        toDate: LocalDate?
    ): List<SaleDto> {
        val allSales = saleRepository.findAll().asSequence()

        return allSales
            .filter { warehouseId == null || it.warehouse.id == warehouseId }
            .filter { fromDate == null || it.date >= fromDate.atStartOfDay() }
            .filter { toDate == null || it.date <= toDate.atTime(23, 59, 59) }
            .sortedByDescending { it.date }
            .map { it.toDto() }
            .toList()
    }


    override fun getSalesByManagerId(managerId: Long): List<SaleDto> {
        return saleRepository.findAll()
            .filter { it.manager.id == managerId }
            .map { it.toDto() }
    }

    override fun getSaleById(id: Long): SaleDto? {
        return saleRepository.findByIdOrNull(id)?.toDto()
    }

    override fun deleteSale(id: Long): ServiceResult<Unit> {
        return if (saleRepository.existsById(id)) {
            saleRepository.deleteById(id)
            ServiceResult.Success(Unit)
        } else {
            ServiceResult.NotFound
        }
    }

    override fun updateSale(id: Long, items: List<SaleItemDto>): ServiceResult<SaleDto> {
        val savedSale = saleRepository.findByIdOrNull(id) ?: return ServiceResult.NotFound

        val existingSaleItems = savedSale.items.associateBy { it.inventoryItem.id }
        val updatedSaleItems = mutableSetOf<SaleItem>()

        items.forEach { itemDto ->
            val existingItem = existingSaleItems[itemDto.inventoryItemId]

            if (existingItem != null) {
                updatedSaleItems.add(
                    existingItem.copy(quantity = itemDto.quantity, price = itemDto.price)
                )
            } else {
                val inventoryItem = inventoryItemRepository.findByIdOrNull(itemDto.inventoryItemId) ?: return@forEach
                val newItem = SaleItem(
                    sale = savedSale,
                    inventoryItem = inventoryItem,
                    quantity = itemDto.quantity,
                    price = itemDto.price
                )
                updatedSaleItems.add(newItem)
            }
        }

        val removedItems = savedSale.items.filter { it !in updatedSaleItems }
        saleItemRepository.deleteAll(removedItems)

        val updatedTotalAmount = updatedSaleItems.sumOf { it.quantity * it.price.toDouble() }.toFloat()

        val updatedSale = savedSale.copy(
            items = updatedSaleItems,
            totalAmount = updatedTotalAmount
        )

        val newSale = saleRepository.save(updatedSale)
        return ServiceResult.Success(newSale.toDto())
    }

    override fun getStatistics(): StatsDto {
        // val currentUser =  currentUserProvider.getCurrentUser()

        val warehouseIds = organizationService.getCurrentAdminOrganizations().flatMap { organization ->
            warehouseRepository.getWarehousesByOrganizationId(requireNotNull(organization.id)).map { it.id }
        }

        val salesByDate = saleRepository.getSalesByDate(warehouseIds)
        val topWarehouses = saleRepository.getTopWarehouses(warehouseIds)
        val topProducts = saleRepository.getTopProducts(warehouseIds, PageRequest.of(0, 1))
        val totalSalesAmount = salesByDate.sumOf { it.totalAmount.toDouble() }.toFloat()
        val totalSalesCount = salesByDate.size

        return StatsDto(
            salesByDate = salesByDate,
            topWarehouses = topWarehouses,
            topProducts = topProducts,
            totalSalesAmount = totalSalesAmount,
            totalSalesCount = totalSalesCount
        )
    }
}