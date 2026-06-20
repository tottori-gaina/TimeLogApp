package com.yourname.timelogapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yourname.timelogapp.storage.TimeLogStorage
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDateTime

@Composable
fun AddLogScreen(onBack: () -> Unit) {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    var startHour by remember { mutableStateOf(now.hour.toString().padStart(2, '0')) }
    var startMinute by remember { mutableStateOf(now.minute.toString().padStart(2, '0')) }
    var endHour by remember { mutableStateOf("") }
    var endMinute by remember { mutableStateOf("") }
    var activity by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ログを追加") })
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

            // 終了時刻
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
                    val sh = startHour.toIntOrNull()
                    val sm = startMinute.toIntOrNull()
                    val eh = endHour.toIntOrNull()
                    val em = endMinute.toIntOrNull()

                    // 終了時刻は両方入力されているか、両方空かのどちらか
                    val endFilled = endHour.isNotBlank() || endMinute.isNotBlank()

                    when {
                        sh == null || sh !in 0..23 -> errorMessage = "開始時（0〜23）を入力してください"
                        sm == null || sm !in 0..59 -> errorMessage = "開始分（0〜59）を入力してください"
                        endFilled && (eh == null || eh !in 0..23) -> errorMessage = "終了時（0〜23）を入力してください"
                        endFilled && (em == null || em !in 0..59) -> errorMessage = "終了分（0〜59）を入力してください"
                        activity.isBlank() -> errorMessage = "活動内容を入力してください"
                        else -> {
                            val startDateTime = LocalDateTime(
                                now.year, now.month, now.day, sh, sm
                            )
                            val endDateTime = if (endFilled && eh != null && em != null) {
                                LocalDateTime(now.year, now.month, now.day, eh, em)
                            } else null

                            TimeLogStorage.addLog(startDateTime, endDateTime, activity)
                            onBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("保存")
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("キャンセル")
            }
        }
    }
}