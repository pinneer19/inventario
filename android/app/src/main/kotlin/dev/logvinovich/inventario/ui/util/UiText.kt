package dev.logvinovich.inventario.ui.util

import androidx.annotation.StringRes

sealed class UiText {
    data class Plain(val message: String): UiText()

    data class Resource(@StringRes val messageRes: Int): UiText()
}