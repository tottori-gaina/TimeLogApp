package com.yourname.timelogapp

import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme
import com.yourname.timelogapp.screen.AddLogScreen
import com.yourname.timelogapp.screen.DayLogScreen
import com.yourname.timelogapp.screen.HistoryScreen
import com.yourname.timelogapp.screen.HomeScreen
import kotlinx.datetime.LocalDate

@Composable
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf("home") }
        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

        when (currentScreen) {
            "home" -> HomeScreen(
                onAddClick = { currentScreen = "add" },
                onHistoryClick = { currentScreen = "history" }
            )
            "add" -> AddLogScreen(onBack = { currentScreen = "home" })
            "history" -> HistoryScreen(
                onDateClick = { date ->
                    selectedDate = date
                    currentScreen = "dayLog"
                },
                onBack = { currentScreen = "home" }
            )
            "dayLog" -> selectedDate?.let { date ->
                DayLogScreen(
                    date = date,
                    onBack = { currentScreen = "history" }
                )
            }
        }
    }
}