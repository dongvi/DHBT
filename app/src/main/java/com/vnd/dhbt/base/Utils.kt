package com.vnd.dhbt

import com.vnd.dhbt.base.models.TimeAlarm

const val ALARM_ACTION = "ALARM_ACTION"
const val HOUR = "HOUR"
const val MINUTE = "MINUTE"
const val ALARM_ID = 0
const val ALARM_TIME = "ALARM_TIME"

fun Boolean.toInt() = if (this) 0 else 1

fun Int.toBoolean() = this == 0

fun convertDataToString(data: MutableList<TimeAlarm>): String {
    var string = ""
    data.forEach {
        string += "${it.id}:${it.hour}:${it.minute}:${it.dayOfWeek}:${it.status.toInt()}" + if(data.last() != it) "@" else ""
    }
    return string
}

fun convertStringToData(string: String): List<TimeAlarm> {
    if(string.trim().isEmpty()) return mutableListOf()

    return string.trim().split("@").map {
        val data = it.trim().split(":")
        TimeAlarm(data[0].toInt(), data[1].toInt(), data[2].toInt(), data[3], data[4].toInt().toBoolean())
    }
}
