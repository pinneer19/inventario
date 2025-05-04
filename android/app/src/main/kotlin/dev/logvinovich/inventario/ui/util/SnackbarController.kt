package dev.logvinovich.inventario.ui.util

import androidx.annotation.StringRes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackbarEvent(
    val message: String? = null,
    @StringRes val messageRes: Int? = null,
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val name: String,
    val action: () -> Unit
)

object SnackbarController {
    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }

    suspend fun sendMessageEvent(message: String) {
        sendEvent(
            SnackbarEvent(message = message)
        )
    }

    suspend fun sendMessageResEvent(@StringRes messageRes: Int) {
        sendEvent(
            SnackbarEvent(messageRes = messageRes)
        )
    }
}