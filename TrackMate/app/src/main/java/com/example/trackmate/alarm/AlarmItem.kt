package com.example.trackmate.alarm

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String = "Get Your Tracking Going On!!",
)
