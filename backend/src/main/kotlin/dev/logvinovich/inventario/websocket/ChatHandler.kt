package dev.logvinovich.inventario.websocket

import dev.logvinovich.inventario.entity.Message
import dev.logvinovich.inventario.model.toDto
import dev.logvinovich.inventario.repository.MessageRepository
import dev.logvinovich.inventario.repository.OrganizationRepository
import dev.logvinovich.inventario.repository.UserRepository
import dev.logvinovich.inventario.repository.WarehouseRepository
import dev.logvinovich.inventario.security.JwtUtil
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ChatHandler(
    private val messageRepository: MessageRepository,
    private val organizationRepository: OrganizationRepository,
    private val warehouseRepository: WarehouseRepository,
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil
) : TextWebSocketHandler() {

    private val sessionsByOrganization = mutableMapOf<Long, MutableList<WebSocketSession>>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val params = extractParams(session)

        val token = params["token"]
        val organizationId = params["organizationId"]?.toLongOrNull()
        val warehouseId = params["warehouseId"]?.toLongOrNull()

        if (token != null) {
            try {
                val claims = jwtUtil.extractAllClaims(token)
                val userId = (claims["userId"] as? Number)?.toLong() ?: return

                val sessionOrganizationId = if (organizationId != null) {
                    organizationId
                } else {
                    val warehouse = warehouseRepository.findByIdOrNull(warehouseId) ?: return
                    warehouse.organization.id
                }
                session.attributes["userId"] = userId
                session.attributes["organizationId"] = sessionOrganizationId

                val orgSessions = sessionsByOrganization.getOrPut(sessionOrganizationId ?: -1) { mutableListOf() }
                orgSessions.add(session)

                val recentMessages = messageRepository.findAllByOrganizationId(requireNotNull(sessionOrganizationId))
                    .sortedBy { it.timestamp }

                recentMessages.forEach { msg ->
                    val json = Json.encodeToString(msg.toDto())
                    session.sendMessage(TextMessage(json))
                }
            } catch (e: Exception) {
                session.close(CloseStatus.BAD_DATA)
            }
        } else {
            session.close(CloseStatus.BAD_DATA)
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val userId = session.attributes["userId"] as? Long ?: return
        val organizationId = session.attributes["organizationId"] as? Long

        val user = userRepository.findByIdOrNull(userId) ?: return

        val content = message.payload

        val organization = organizationRepository.findByIdOrNull(organizationId) ?: return

        val savedMessage = messageRepository.save(
            Message(sender = user, content = content, organization = organization)
        )

        val json = Json.encodeToString(savedMessage.toDto())

        val orgSessions = sessionsByOrganization[organizationId] ?: return
        orgSessions.forEach {
            if (it.isOpen) {
                it.sendMessage(TextMessage(json))
            }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val organizationId = session.attributes["organizationId"] as? Long ?: return
        sessionsByOrganization[organizationId]?.remove(session)
        if (sessionsByOrganization[organizationId]?.isEmpty() == true) {
            sessionsByOrganization.remove(organizationId)
        }
    }

    private fun extractParams(session: WebSocketSession): Map<String, String> {
        val query = session.uri?.query ?: return emptyMap()
        return query.split("&").mapNotNull { it.split("=").takeIf { it.size == 2 } }.associate { it[0] to it[1] }
    }
}