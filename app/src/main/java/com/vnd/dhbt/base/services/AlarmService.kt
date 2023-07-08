package com.vnd.dhbt.base.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.vnd.dhbt.ALARM_ACTION
import com.vnd.dhbt.base.models.TimeAlarm
import java.util.*

class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val alarmList = convertStringToData(AppSharedPreferences(this).getData())
//
//        for (alarm in alarmList) {
//            if (alarm.status) setAlarm(this, alarm)
//            else cancelAlarm(this, alarm.id)
//        }
//
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.cancel(ALARM_ID)
        return START_STICKY
    }

    companion object {
        fun setAlarm(context: Context, alarm: TimeAlarm) {
            val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java).apply { action = ALARM_ACTION }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarm.id,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarm.hour)
                set(Calendar.MINUTE, alarm.minute)
                set(Calendar.SECOND, 0)
                if(
                    timeInMillis < System.currentTimeMillis() ||
                    get(Calendar.DAY_OF_WEEK) !in alarm.dayOfWeek.trim().split(",").map { it.toInt() }
                ) add(Calendar.DATE, 1)
            }

            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    calendar.timeInMillis,
                    pendingIntent
                ),
                pendingIntent
            )
        }

        fun cancelAlarm(context: Context, id: Int) {
            val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java).apply { action = ALARM_ACTION }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.cancel(pendingIntent)
        }
    }
}
