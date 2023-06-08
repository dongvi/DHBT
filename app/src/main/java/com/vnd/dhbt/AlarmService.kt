package com.vnd.dhbt

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmList = convertStringToData(AppSharedPreferences(this).getData())
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        for (alarm in alarmList) {
            val newIntent = Intent(this, AlarmReceiver::class.java).apply { action = ALARM_ACTION }
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                alarm.id,
                newIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (alarm.status) {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, alarm.hour)
                    set(Calendar.MINUTE, alarm.minute)
                    set(Calendar.SECOND, 0)
                }

                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        calendar.timeInMillis,
                        pendingIntent
                    ),
                    pendingIntent
                )
            } else {
                alarmManager.cancel(pendingIntent)
            }
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ALARM_ID)

        return START_STICKY
    }
}
