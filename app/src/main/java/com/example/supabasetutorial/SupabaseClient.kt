package com.example.supabasetutorial

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import kotlinx.serialization.json.Json


object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://mdnkygiszaemqtmwrvzr.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1kbmt5Z2lzemFlbXF0bXdydnpyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTY3ODM3NTUsImV4cCI6MjA3MjM1OTc1NX0.Kca-JPnunsC3dsslGN5RTrXofl_1rO4AtoH6mP4Z1Fs"
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)
    }

    val json = Json { ignoreUnknownKeys = true }
}