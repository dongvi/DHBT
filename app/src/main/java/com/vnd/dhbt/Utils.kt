package com.vnd.dhbt

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
        string += "${it.id}:${it.hour}:${it.minute}:${it.status.toInt()}" + if(data.last() != it) "@" else ""
    }
    return string
}

fun convertStringToData(string: String): List<TimeAlarm> {
    if(string.trim().isEmpty()) return mutableListOf()

    return string.trim().split("@").map {
        val data = it.trim().split(":").map { it.toInt() }
        TimeAlarm(data[0], data[1], data[2], data[3].toBoolean())
    }
}
