package dev.logvinovich.inventario.model

import dev.logvinovich.inventario.entity.Message
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDto(
    val senderId: Long?,
    val content: String,
    val timestamp: String,
    val senderUsername: String? = null
)

fun Message.toDto() = ChatMessageDto(
    senderId = sender.id,
    senderUsername = sender.username,
    content = content,
    timestamp = timestamp.toString()
)