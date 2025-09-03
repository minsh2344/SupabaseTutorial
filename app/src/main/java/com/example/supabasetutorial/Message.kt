package com.example.supabasetutorial

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val message: String,
    @SerialName("is_from_user")
    val isFromUser: Boolean
)