package com.yourname.timelogapp

import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme
import com.yourname.timelogapp.screen.AddLogScreen
import com.yourname.timelogapp.screen.HomeScreen

@Composable
fun App() {
    MaterialTheme {
        var showAddScreen by remember { mutableStateOf(false) }

        if (showAddScreen) {
            AddLogScreen(onBack = { showAddScreen = false })
        } else {
            HomeScreen(onAddClick = { showAddScreen = true })
        }
    }
}