package com.example.supabasetutorial

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class ChatMessage(
    val id: String = "",
    val content: String,
    @SerialName("user_id")
    val userId: String,
    val username: String,
    @SerialName("created_at")
    val createdAt: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
)
