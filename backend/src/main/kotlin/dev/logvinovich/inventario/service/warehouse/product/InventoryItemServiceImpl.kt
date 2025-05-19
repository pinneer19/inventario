package dev.logvinovich.inventario.service.warehouse.product

import dev.logvinovich.inventario.entity.InventoryItem
import dev.logvinovich.inventario.model.ServiceResult
import dev.logvinovich.inventario.model.UpdateInventoryItemDto
import dev.logvinovich.inventario.repository.InventoryItemRepository
import dev.logvinovich.inventario.repository.ProductRepository
import dev.logvinovich.inventario.repository.WarehouseRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class InventoryItemServiceImpl(
    private val inventoryItemRepository: InventoryItemRepository,
    private val productRepository: ProductRepository,
    private val warehouseRepository: WarehouseRepository
) : InventoryItemService {
    override fun createInventoryItem(item: UpdateInventoryItemDto): ServiceResult<InventoryItem> {
        val itemProduct = productRepository.findByIdOrNull(item.productId)
        val itemWarehouse = warehouseRepository.findByIdOrNull(item.warehouseId)

        return if (itemProduct == null || itemWarehouse == null) {
            ServiceResult.BadRequest
        } else {
            val saveResult = inventoryItemRepository.save(item.toInventoryItem(itemWarehouse, itemProduct))
            ServiceResult.Success(saveResult)
        }
    }

    override fun updateInventoryItem(id: Long, item: UpdateInventoryItemDto): ServiceResult<InventoryItem> {
        return if (inventoryItemRepository.existsById(id)) {
            val existingItem = inventoryItemRepository.findById(id).get()

            val itemProduct = productRepository.findByIdOrNull(item.productId)

            val updatedItem = existingItem.copy(
                product = itemProduct ?: existingItem.product,
                price = item.price,
                quantity = item.quantity,
            )
            inventoryItemRepository.save(updatedItem)

            ServiceResult.Success(updatedItem)
        } else {
            ServiceResult.NotFound
        }
    }

    override fun getInventoryItemsByWarehouseId(warehouseId: Long): List<InventoryItem> {
        return inventoryItemRepository.findAllByWarehouseId(warehouseId)
    }

    override fun deleteInventoryItem(itemId: Long): ServiceResult<Unit> {
        return if (inventoryItemRepository.existsById(itemId)) {
            inventoryItemRepository.deleteById(itemId)
            ServiceResult.Success(Unit)
        } else {
            ServiceResult.NotFound
        }
    }
}