package com.example.supabasetutorial

import kotlinx.serialization.Serializable

@Serializable
data class Instrument(
    val id: Int,
    val name: String,
)
