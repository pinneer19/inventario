package dev.logvinovich.inventario.data.api

import dev.logvinovich.inventario.data.JwtManager
import dev.logvinovich.inventario.data.model.ChatMessageDTO
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatWebSocketClient @Inject constructor(
    private val httpClient: HttpClient,
    private val jwtManager: JwtManager,
    private val baseUrl: String
) {
    private val _messages = MutableSharedFlow<ChatMessageDTO>()
    val messages = _messages.asSharedFlow()

    private var _session: WebSocketSession? = null

    suspend fun connect(organizationId: Long?, warehouseId: Long?) {
        val token = jwtManager.getTokens()?.accessToken ?: return
        val socketUrl =
            "$baseUrl?token=$token&organizationId=$organizationId&warehouseId=$warehouseId"

        httpClient.webSocket(urlString = socketUrl) {
            _session = this

            while (isActive) {
                val frame = incoming.receive()
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    val msg = Json.decodeFromString<ChatMessageDTO>(text)

                    _messages.emit(msg)
                }
            }
        }
    }

    suspend fun sendMessage(content: String) {
        _session?.send(Frame.Text(content))
    }
}