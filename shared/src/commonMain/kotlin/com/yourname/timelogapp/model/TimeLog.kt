package com.yourname.timelogapp.model

import kotlinx.datetime.LocalDateTime

data class TimeLog(
    val id: Long = 0,
    val dateTime: LocalDateTime,      // 開始時刻
    val endDateTime: LocalDateTime?,  // 終了時刻（nullable・未終了の場合はnull）
    val activity: String
)