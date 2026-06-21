package com.yourname.timelogapp

import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme
import com.yourname.timelogapp.model.TimeLog
import com.yourname.timelogapp.screen.AddLogScreen
import com.yourname.timelogapp.screen.DayLogScreen
import com.yourname.timelogapp.screen.EditLogScreen
import com.yourname.timelogapp.screen.HistoryScreen
import com.yourname.timelogapp.screen.HomeScreen
import kotlinx.datetime.LocalDate

@Composable
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf("home") }
        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
        var selectedLog by remember { mutableStateOf<TimeLog?>(null) }

        when (currentScreen) {
            "home" -> HomeScreen(
                onAddClick = { currentScreen = "add" },
                onHistoryClick = { currentScreen = "history" },
                onEditClick = { log ->
                    selectedLog = log
                    currentScreen = "edit"
                }
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
                    onBack = { currentScreen = "history" },
                    onEditClick = { log ->
                        selectedLog = log
                        currentScreen = "edit"
                    }
                )
            }
            "edit" -> selectedLog?.let { log ->
                EditLogScreen(
                    log = log,
                    onBack = { currentScreen = "home" }
                )
            }
        }
    }
}