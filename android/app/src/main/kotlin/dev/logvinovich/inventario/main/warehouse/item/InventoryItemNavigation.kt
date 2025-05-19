package dev.logvinovich.inventario.main.warehouse.item

import android.net.Uri
import android.os.Bundle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.logvinovich.inventario.domain.model.InventoryItem
import dev.logvinovich.inventario.main.warehouse.item.viewmodel.InventoryItemViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Serializable
data class InventoryItemDestination(
    val warehouseId: Long,
    val inventoryItem: InventoryItem? = null
)

fun NavGraphBuilder.inventoryItemScreen(navController: NavController) {
    composable<InventoryItemDestination>(
        typeMap = mapOf(typeOf<InventoryItem?>() to InventoryItemNavType)
    ) {
        val (warehouseId, inventoryItem) = it.toRoute<InventoryItemDestination>()
        val viewModel = hiltViewModel<InventoryItemViewModel, InventoryItemViewModel.Factory>(
            creationCallback = { factory -> factory.create(inventoryItem, warehouseId) }
        )
        InventoryItemScreen(
            viewModel = viewModel,
            onNavigateUp = { item ->
                navController.popBackStack()
                item?.let {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "savedItem",
                        value = Json.encodeToString(it)
                    )
                }
            }
        )
    }
}

fun NavController.navigateToInventoryItem(warehouseId: Long, inventoryItem: InventoryItem?) =
    navigate(InventoryItemDestination(warehouseId, inventoryItem))


val InventoryItemNavType = object : NavType<InventoryItem?>(isNullableAllowed = true) {
    override fun get(
        bundle: Bundle,
        key: String
    ): InventoryItem? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): InventoryItem {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: InventoryItem?): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: InventoryItem?
    ) {
        bundle.putString(key, Json.encodeToString(value))
    }
}