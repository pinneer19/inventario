package dev.logvinovich.inventario.data.model

import dev.logvinovich.inventario.domain.model.ChatMessage
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDTO(
    val senderId: Long? = null,
    val senderUsername: String? = null,
    val content: String,
    val timestamp: String
) {
    fun toChatMessage(currentUserId: Long) = ChatMessage(
        senderId = senderId,
        senderUsername = senderUsername.orEmpty(),
        content = content,
        timestamp = timestamp,
        isOwnMessage = senderId == currentUserId
    )
}