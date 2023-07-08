package com.vnd.dhbt.base.models

data class TimeAlarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    var dayOfWeek: String,
    var status: Boolean = true
)
