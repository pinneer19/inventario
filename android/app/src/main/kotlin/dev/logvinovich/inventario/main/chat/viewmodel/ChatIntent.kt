package dev.logvinovich.inventario.main.chat.viewmodel

sealed interface ChatIntent {
    data class UpdateMessage(val message: String) : ChatIntent
    data object SendMessage : ChatIntent
}