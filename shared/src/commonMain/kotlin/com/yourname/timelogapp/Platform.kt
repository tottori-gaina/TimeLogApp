package com.yourname.timelogapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform