package com.example.supabasetutorial

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageInsert(
    val content: String,
    @SerialName("user_id")
    val userId: String,
    val username: String
)
