package com.vnd.dhbt

import android.content.Context

class AppSharedPreferences(context: Context) {
    companion object {
        const val NAME = "VND"
        const val DATA = "ALARM_TIME"
    }

    private val sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun getData(): String = sharedPreferences.getString(DATA, "") ?: ""

    fun saveData(data: String) = sharedPreferences.edit().putString(DATA, data).apply()
}
