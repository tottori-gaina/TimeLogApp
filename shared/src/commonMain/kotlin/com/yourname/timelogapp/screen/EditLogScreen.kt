package com.yourname.timelogapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yourname.timelogapp.model.TimeLog
import com.yourname.timelogapp.storage.TimeLogStorage
import kotlinx.datetime.LocalDateTime

@Composable
fun EditLogScreen(log: TimeLog, onBack: () -> Unit) {
    var startHour by remember { mutableStateOf(log.dateTime.hour.toString().padStart(2, '0')) }
    var startMinute by remember { mutableStateOf(log.dateTime.minute.toString().padStart(2, '0')) }
    var endHour by remember { mutableStateOf(log.endDateTime?.hour?.toString()?.padStart(2, '0') ?: "") }
    var endMinute by remember { mutableStateOf(log.endDateTime?.minute?.toString()?.padStart(2, '0') ?: "") }
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
            Text("開始時刻", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = startHour,
                    onValueChange = { if (it.length <= 2) startHour = it },
                    label = { Text("時") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = startMinute,
                    onValueChange = { if (it.length <= 2) startMinute = it },
                    label = { Text("分") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Text("終了時刻（任意）", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = endHour,
                    onValueChange = { if (it.length <= 2) endHour = it },
                    label = { Text("時") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = endMinute,
                    onValueChange = { if (it.length <= 2) endMinute = it },
                    label = { Text("分") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

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
                    val sh = startHour.toIntOrNull()
                    val sm = startMinute.toIntOrNull()
                    val eh = endHour.toIntOrNull()
                    val em = endMinute.toIntOrNull()
                    val endFilled = endHour.isNotBlank() || endMinute.isNotBlank()

                    when {
                        sh == null || sh !in 0..23 -> errorMessage = "開始時（0〜23）を入力してください"
                        sm == null || sm !in 0..59 -> errorMessage = "開始分（0〜59）を入力してください"
                        endFilled && (eh == null || eh !in 0..23) -> errorMessage = "終了時（0〜23）を入力してください"
                        endFilled && (em == null || em !in 0..59) -> errorMessage = "終了分（0〜59）を入力してください"
                        activity.isBlank() -> errorMessage = "活動内容を入力してください"
                        else -> {
                            val startDateTime = LocalDateTime(
                                log.dateTime.year, log.dateTime.month, log.dateTime.day, sh, sm
                            )
                            val endDateTime = if (endFilled && eh != null && em != null) {
                                LocalDateTime(log.dateTime.year, log.dateTime.month, log.dateTime.day, eh, em)
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