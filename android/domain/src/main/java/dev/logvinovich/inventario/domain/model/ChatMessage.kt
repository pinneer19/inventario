package dev.logvinovich.inventario.domain.model

data class ChatMessage(
    val senderId: Long? = null,
    val senderUsername: String,
    val isOwnMessage: Boolean,
    val content: String,
    val timestamp: String
)