package com.yourname.timelogapp.storage

import com.yourname.timelogapp.model.TimeLog
import kotlinx.datetime.LocalDateTime

object TimeLogStorage {
    private val logs = mutableListOf<TimeLog>()
    private var nextId = 1L

    fun addLog(dateTime: LocalDateTime, activity: String) {
        logs.add(
            TimeLog(
                id = nextId++,
                dateTime = dateTime,
                activity = activity
            )
        )
    }

    fun getLogsForToday(): List<TimeLog> {
        return logs.sortedBy { it.dateTime }
    }

    fun deleteLog(id: Long) {
        logs.removeAll { it.id == id }
    }
}