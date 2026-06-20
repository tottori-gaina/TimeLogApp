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

    var hour by remember { mutableStateOf(now.hour.toString().padStart(2, '0')) }
    var minute by remember { mutableStateOf(now.minute.toString().padStart(2, '0')) }
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = hour,
                    onValueChange = { if (it.length <= 2) hour = it },
                    label = { Text("時") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = minute,
                    onValueChange = { if (it.length <= 2) minute = it },
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
                    val h = hour.toIntOrNull()
                    val m = minute.toIntOrNull()
                    when {
                        h == null || h !in 0..23 -> errorMessage = "時は0〜23で入力してください"
                        m == null || m !in 0..59 -> errorMessage = "分は0〜59で入力してください"
                        activity.isBlank() -> errorMessage = "活動内容を入力してください"
                        else -> {
                            val dateTime = LocalDateTime(
                                now.year, now.monthNumber, now.dayOfMonth, h, m
                            )
                            TimeLogStorage.addLog(dateTime, activity)
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