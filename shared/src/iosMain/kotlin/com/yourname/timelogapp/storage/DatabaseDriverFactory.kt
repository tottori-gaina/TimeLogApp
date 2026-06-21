package com.yourname.timelogapp.storage

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.yourname.timelogapp.db.TimeLogDatabase

actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(TimeLogDatabase.Schema, "timelog.db")
}