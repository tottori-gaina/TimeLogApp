package com.yourname.timelogapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourname.timelogapp.storage.TimeLogStorage
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@Composable
fun DayLogScreen(date: LocalDate, onBack: () -> Unit) {
    var logs by remember { mutableStateOf(TimeLogStorage.getLogsForDate(date)) }

    val dateStr = "${date.year}/${date.month.number.toString().padStart(2, '0')}/${date.day.toString().padStart(2, '0')}"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(dateStr) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("< 戻る")
                    }
                }
            )
        }
    ) { padding ->
        if (logs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("この日のログはありません")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(logs) { log ->
                    TimeLogItem(
                        log = log,
                        onDelete = {
                            TimeLogStorage.deleteLog(log.id)
                            logs = TimeLogStorage.getLogsForDate(date)
                        }
                    )
                }
            }
        }
    }
}