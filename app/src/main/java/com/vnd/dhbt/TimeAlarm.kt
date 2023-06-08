package com.vnd.dhbt

data class TimeAlarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    var status: Boolean = true
)
