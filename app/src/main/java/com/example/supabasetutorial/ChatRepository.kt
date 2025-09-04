package com.example.supabasetutorial

import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatRepository {

    private val supabase = SupabaseClient.client

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: Flow<List<ChatMessage>> = _messages.asStateFlow()

    suspend fun loadMessages() {
        try {
            val messageList = supabase
                .from("messages")
                .select()
                .decodeList<ChatMessage>()
                .sortedBy { it.createdAt }

            _messages.value = messageList
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun sendMessage(content: String, currentUser: UserInfo?) {
        try {
            val messageToInsert = ChatMessageInsert(
                content = content,
                userId = currentUser?.id ?: "id",
                username = currentUser?.id ?: "id"
            )

            SupabaseClient.client
                .from("messages")
                .insert(messageToInsert)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}