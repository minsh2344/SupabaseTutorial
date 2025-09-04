package com.example.supabasetutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ChatUiState(
    val isLoading: Boolean = false,
    val messageText: String = ""
)

class ChatViewModel : ViewModel() {

    private val chatRepository = ChatRepository()
    private val authRepository = AuthRepository()

    val messages = chatRepository.messages
    val currentUser = authRepository.getCurrentUser()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMessages()
        subscribeToMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            chatRepository.loadMessages()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }


    fun subscribeToMessages() {
        viewModelScope.launch {
            val channel = SupabaseClient.client.realtime.channel("messages")

            val messageFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
                table = "messages"
            }

            messageFlow.onEach { action ->
                chatRepository.loadMessages()

            }.launchIn(viewModelScope)

            channel.subscribe()
        }
    }

    fun sendMessage() {
        val messageText = _uiState.value.messageText.trim()
        if (messageText.isNotEmpty()) {
            viewModelScope.launch {
                chatRepository.sendMessage(messageText, currentUser)
                _uiState.value = _uiState.value.copy(messageText = "")
            }
        }
    }

    fun updateMessageText(text: String) {
        _uiState.value = _uiState.value.copy(messageText = text)
    }
}