package com.yourname.timelogapp.model

import kotlinx.datetime.LocalDateTime

data class TimeLog(
    val id: Long = 0,
    val dateTime: LocalDateTime,
    val activity: String
)