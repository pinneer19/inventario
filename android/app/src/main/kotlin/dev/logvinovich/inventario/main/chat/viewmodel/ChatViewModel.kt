package dev.logvinovich.inventario.main.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.inventario.data.api.ChatWebSocketClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@HiltViewModel(assistedFactory = ChatViewModel.Factory::class)
class ChatViewModel @AssistedInject constructor(
    @Assisted private val userId: Long,
    @Assisted("organizationId") private val organizationId: Long?,
    @Assisted("warehouseId") private val warehouseId: Long?,
    private val client: ChatWebSocketClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            client.connect(organizationId, warehouseId)
        }

        viewModelScope.launch {
            client.messages.collect { msg ->
                _uiState.update { it.copy(messages = it.messages + msg.toChatMessage(userId)) }
            }
        }
    }

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            ChatIntent.SendMessage -> sendMessage()
            is ChatIntent.UpdateMessage -> updateMessage(intent.message)
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            client.sendMessage(_uiState.value.input)
            _uiState.update { it.copy(input = "") }
        }
    }

    private fun updateMessage(message: String) {
        _uiState.update { it.copy(input = message) }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            userId: Long,
            @Assisted("organizationId") organizationId: Long?,
            @Assisted("warehouseId") warehouseId: Long?
        ): ChatViewModel
    }
}