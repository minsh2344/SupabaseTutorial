package com.example.supabasetutorial

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.supabasetutorial.ui.theme.SupabaseTutorialTheme
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupabaseTutorialTheme {
                ChatApp()
            }
        }
    }
}

@Composable
fun ChatApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val startDestination = if (authViewModel.isLoggedIn()) "chat" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToChat = {
                    navController.navigate("chat") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("chat") {
            ChatScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(timestamp: String): String {
    val odt = OffsetDateTime.parse(timestamp.replace(" ", "T"))
    val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
    return odt.format(formatter)
}