package com.yourname.timelogapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourname.timelogapp.component.WheelPicker
import com.yourname.timelogapp.model.TimeLog
import com.yourname.timelogapp.storage.TimeLogStorage
import kotlinx.datetime.LocalDateTime

@Composable
fun EditLogScreen(log: TimeLog, onBack: () -> Unit) {
    val hours = (0..23).map { it.toString().padStart(2, '0') }
    val minutes = (0..59).map { it.toString().padStart(2, '0') }

    var startHourIndex by remember { mutableStateOf(log.dateTime.hour) }
    var startMinuteIndex by remember { mutableStateOf(log.dateTime.minute) }
    var hasEndTime by remember { mutableStateOf(log.endDateTime != null) }
    var endHourIndex by remember { mutableStateOf(log.endDateTime?.hour ?: log.dateTime.hour) }
    var endMinuteIndex by remember { mutableStateOf(log.endDateTime?.minute ?: log.dateTime.minute) }
    var activity by remember { mutableStateOf(log.activity) }
    var errorMessage by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("削除確認") },
            text = { Text("このログを削除しますか？") },
            confirmButton = {
                TextButton(onClick = {
                    TimeLogStorage.deleteLog(log.id)
                    onBack()
                }) { Text("削除", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("キャンセル") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ログを編集") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("< 戻る") }
                },
                actions = {
                    TextButton(onClick = { showDeleteDialog = true }) {
                        Text("削除", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 開始時刻
            Text("開始時刻", style = MaterialTheme.typography.labelLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WheelPicker(
                    items = hours,
                    selectedIndex = startHourIndex,
                    onSelectedIndexChange = { startHourIndex = it },
                    modifier = Modifier.weight(1f)
                )
                Text(":", style = MaterialTheme.typography.headlineMedium)
                WheelPicker(
                    items = minutes,
                    selectedIndex = startMinuteIndex,
                    onSelectedIndexChange = { startMinuteIndex = it },
                    modifier = Modifier.weight(1f)
                )
            }

            // 終了時刻
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("終了時刻", style = MaterialTheme.typography.labelLarge)
                Switch(
                    checked = hasEndTime,
                    onCheckedChange = { hasEndTime = it }
                )
            }

            if (hasEndTime) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WheelPicker(
                        items = hours,
                        selectedIndex = endHourIndex,
                        onSelectedIndexChange = { endHourIndex = it },
                        modifier = Modifier.weight(1f)
                    )
                    Text(":", style = MaterialTheme.typography.headlineMedium)
                    WheelPicker(
                        items = minutes,
                        selectedIndex = endMinuteIndex,
                        onSelectedIndexChange = { endMinuteIndex = it },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 活動内容
            OutlinedTextField(
                value = activity,
                onValueChange = { activity = it },
                label = { Text("活動内容") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    when {
                        activity.isBlank() -> errorMessage = "活動内容を入力してください"
                        else -> {
                            val startDateTime = LocalDateTime(
                                log.dateTime.year, log.dateTime.month, log.dateTime.day,
                                startHourIndex, startMinuteIndex
                            )
                            val endDateTime = if (hasEndTime) {
                                LocalDateTime(
                                    log.dateTime.year, log.dateTime.month, log.dateTime.day,
                                    endHourIndex, endMinuteIndex
                                )
                            } else null
                            TimeLogStorage.updateLog(log.id, startDateTime, endDateTime, activity)
                            onBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("保存")
            }
        }
    }
}