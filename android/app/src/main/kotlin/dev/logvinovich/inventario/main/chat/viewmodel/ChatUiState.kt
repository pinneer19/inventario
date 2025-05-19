package dev.logvinovich.inventario.main.chat.viewmodel

import dev.logvinovich.inventario.data.model.ChatMessageDTO
import dev.logvinovich.inventario.domain.model.ChatMessage

data class ChatUiState(
    val input: String = "",
    val loading: Boolean = false,
    val messages: List<ChatMessage> = emptyList()
)