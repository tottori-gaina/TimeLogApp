package com.yourname.timelogapp.screen

import androidx.compose.foundation.clickable
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
fun HistoryScreen(onDateClick: (LocalDate) -> Unit, onBack: () -> Unit) {
    var dates by remember { mutableStateOf(TimeLogStorage.getAvailableDates()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("履歴") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("< 戻る")
                    }
                }
            )
        },
    ) { padding ->
        if (dates.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("まだ履歴がありません")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dates) { date ->
                    val logCount = TimeLogStorage.getLogsForDate(date).size
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDateClick(date) }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${date.year}/${date.month.number.toString().padStart(2, '0')}/${date.day.toString().padStart(2, '0')}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                                Text(
                                    text = "${logCount}件",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                val total = TimeLogStorage.getTotalMinutesForDate(date)
                                Text(
                                    text = "${total / 60}時間${total % 60}分",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}