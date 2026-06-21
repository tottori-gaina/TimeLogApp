package com.yourname.timelogapp.storage

import app.cash.sqldelight.db.SqlDriver
import com.yourname.timelogapp.db.TimeLogDatabase

expect fun createDriver(): SqlDriver

object DatabaseHelper {
    val database: TimeLogDatabase by lazy {
        TimeLogDatabase(createDriver())
    }
}