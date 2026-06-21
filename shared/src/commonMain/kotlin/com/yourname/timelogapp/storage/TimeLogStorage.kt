package com.yourname.timelogapp.storage

import com.yourname.timelogapp.model.TimeLog
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object TimeLogStorage {

    private val queries = DatabaseHelper.database.timeLogQueries

    fun addLog(dateTime: LocalDateTime, endDateTime: LocalDateTime?, activity: String) {
        queries.insert(
            start_time = dateTime.toString(),
            end_time = endDateTime?.toString(),
            activity = activity
        )
    }

    fun getLogsForToday(): List<TimeLog> {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date.toString()
        return queries.selectByDate(today).executeAsList().map { it.toModel() }
    }

    fun deleteLog(id: Long) {
        queries.delete(id)
    }

    fun updateLog(id: Long, dateTime: LocalDateTime, endDateTime: LocalDateTime?, activity: String) {
        queries.update(
            start_time = dateTime.toString(),
            end_time = endDateTime?.toString(),
            activity = activity,
            id = id
        )
    }

    fun getAvailableDates(): List<LocalDate> {
        return queries.selectDates().executeAsList().map {
            LocalDate.parse(it)
        }
    }

    fun getLogsForDate(date: LocalDate): List<TimeLog> {
        return queries.selectByDate(date.toString()).executeAsList().map { it.toModel() }
    }

    private fun com.yourname.timelogapp.db.TimeLog.toModel() = TimeLog(
        id = id,
        dateTime = LocalDateTime.parse(start_time),
        endDateTime = end_time?.let { LocalDateTime.parse(it) },
        activity = activity
    )
}