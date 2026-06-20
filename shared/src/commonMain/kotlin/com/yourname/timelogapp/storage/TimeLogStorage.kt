package com.yourname.timelogapp.storage

import com.yourname.timelogapp.model.TimeLog
import kotlinx.datetime.LocalDate
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

    // 日付一覧を取得（ログがある日付のみ、新しい順）
    fun getAvailableDates(): List<LocalDate> {
        return logs
            .map { it.dateTime.date }
            .distinct()
            .sortedDescending()
    }

    // 特定の日付のログを取得
    fun getLogsForDate(date: LocalDate): List<TimeLog> {
        return logs
            .filter { it.dateTime.date == date }
            .sortedBy { it.dateTime }
    }
}