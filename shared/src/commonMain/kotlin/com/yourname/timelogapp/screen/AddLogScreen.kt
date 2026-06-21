package com.yourname.timelogapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourname.timelogapp.component.WheelPicker
import com.yourname.timelogapp.storage.TimeLogStorage
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDateTime

@Composable
fun AddLogScreen(onBack: () -> Unit) {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val hours = (0..23).map { it.toString().padStart(2, '0') }
    val minutes = (0..59).map { it.toString().padStart(2, '0') }

    var startHourIndex by remember { mutableStateOf(now.hour) }
    var startMinuteIndex by remember { mutableStateOf(now.minute) }
    var endHourIndex by remember { mutableStateOf<Int?>(null) }
    var endMinuteIndex by remember { mutableStateOf<Int?>(null) }
    var hasEndTime by remember { mutableStateOf(false) }
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
                    onCheckedChange = {
                        hasEndTime = it
                        if (it) {
                            endHourIndex = now.hour
                            endMinuteIndex = now.minute
                        } else {
                            endHourIndex = null
                            endMinuteIndex = null
                        }
                    }
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
                        selectedIndex = endHourIndex ?: now.hour,
                        onSelectedIndexChange = { endHourIndex = it },
                        modifier = Modifier.weight(1f)
                    )
                    Text(":", style = MaterialTheme.typography.headlineMedium)
                    WheelPicker(
                        items = minutes,
                        selectedIndex = endMinuteIndex ?: now.minute,
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
                                now.year, now.month, now.day, startHourIndex, startMinuteIndex
                            )
                            val endDateTime = if (hasEndTime && endHourIndex != null && endMinuteIndex != null) {
                                LocalDateTime(now.year, now.month, now.day, endHourIndex!!, endMinuteIndex!!)
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