package com.example.supabasetutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supabasetutorial.SupabaseClient.json
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val currentUserId = "currentUserId"
    private val currentUserName = "currentUserName"

    fun loadMessages() {
        viewModelScope.launch {
            try {
                val response =
                    SupabaseClient.client
                        .from("messages")
                        .select()
                        .decodeList<ChatMessage>()

                _messages.value = response.sortedBy { it.createdAt }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun subscribeToMessages() {
        viewModelScope.launch {
            val channel = SupabaseClient.client.realtime.channel("messages")

            val messageFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
                table = "messages"
            }

            messageFlow.onEach { action ->
                val newMessage = json.decodeFromJsonElement<ChatMessage>(action.record)
                val currentMessages = _messages.value.toMutableList()

                if (!currentMessages.any { it.id == newMessage.id }) {
                    currentMessages.add(newMessage)
                    _messages.value = currentMessages.sortedBy { it.createdAt }
                }
            }.launchIn(viewModelScope)

            channel.subscribe()
        }
    }

    suspend fun sendMessage(content: String) {
        try {
            val messageToInsert = ChatMessageInsert(
                content = content,
                userId = currentUserId,
                username = currentUserName
            )

            SupabaseClient.client
                .from("messages")
                .insert(messageToInsert)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}