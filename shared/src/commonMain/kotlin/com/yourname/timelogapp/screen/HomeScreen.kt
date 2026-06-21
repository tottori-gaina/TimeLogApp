package com.yourname.timelogapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourname.timelogapp.model.TimeLog
import com.yourname.timelogapp.storage.TimeLogStorage
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun HomeScreen(onAddClick: () -> Unit, onHistoryClick: () -> Unit, onEditClick: (TimeLog) -> Unit) {
    var logs by remember { mutableStateOf(TimeLogStorage.getLogsForToday()) }
    var totalMinutes by remember { mutableStateOf(TimeLogStorage.getTotalMinutesForDate(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    )) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("タイムログ")
                        Text(
                            text = "合計: ${totalMinutes / 60}時間${totalMinutes % 60}分",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onHistoryClick) {
                        Text("履歴")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { padding ->
        if (logs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("まだログがありません\n+ボタンで追加してください")
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
                            logs = TimeLogStorage.getLogsForToday()
                            totalMinutes = TimeLogStorage.getTotalMinutesForDate(
                                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                            )
                        },
                        onEdit = { onEditClick(log) }
                    )
                }
            }
        }
    }
}

@Composable
fun TimeLogItem(log: TimeLog, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onEdit
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val startTime = "${log.dateTime.hour.toString().padStart(2, '0')}:${log.dateTime.minute.toString().padStart(2, '0')}"
                val endTime = log.endDateTime?.let {
                    "${it.hour.toString().padStart(2, '0')}:${it.minute.toString().padStart(2, '0')}"
                }
                Text(
                    text = if (endTime != null) "$startTime 〜 $endTime" else "$startTime 〜 未終了",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = log.activity,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            TextButton(onClick = onDelete) {
                Text("削除")
            }
        }
    }
}