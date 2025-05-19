package dev.logvinovich.inventario.main.admin.product.details

import android.net.Uri
import android.os.Bundle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.logvinovich.inventario.domain.model.Product
import dev.logvinovich.inventario.main.admin.product.details.viewmodel.ProductDetailsViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Serializable
data class ProductDetailsDestination(
    val organizationId: Long,
    val product: Product? = null
)

fun NavGraphBuilder.productDetailsScreen(navController: NavController) {
    composable<ProductDetailsDestination>(
        typeMap = mapOf(
            typeOf<Product?>() to ProductNavType
        )
    ) { navBackStackEntry ->
        val (organizationId, product) = navBackStackEntry.toRoute<ProductDetailsDestination>()
        val viewModel = hiltViewModel<ProductDetailsViewModel, ProductDetailsViewModel.Factory>(
            creationCallback = { factory -> factory.create(product, organizationId) }
        )

        ProductDetailsScreen(
            viewModel = viewModel,
            onNavigateUp = { product ->
                navController.popBackStack()
                product?.let {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "savedProduct",
                        value = Json.encodeToString(it)
                    )
                }
            }
        )
    }
}

fun NavController.navigateToProductDetails(organizationId: Long, product: Product?) =
    navigate(ProductDetailsDestination(organizationId, product))

val ProductNavType = object : NavType<Product?>(isNullableAllowed = true) {
    override fun get(
        bundle: Bundle,
        key: String
    ): Product? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): Product {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: Product?): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: Product?
    ) {
        bundle.putString(key, Json.encodeToString(value))
    }
}