package com.vnd.dhbt.base.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.vnd.dhbt.ALARM_ACTION
import com.vnd.dhbt.ALARM_ID
import com.vnd.dhbt.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ALARM_ACTION -> {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notificationChannelId = "vnd.notify"
                val notificationName = "alarm clock"
                val notification: NotificationCompat.Builder =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val notificationChannel = NotificationChannel(
                            notificationChannelId,
                            notificationName,
                            NotificationManager.IMPORTANCE_HIGH
                        )

                        notificationManager.createNotificationChannel(notificationChannel)
                        NotificationCompat.Builder(context, notificationChannelId)
                    } else NotificationCompat.Builder(context)

//                val runMainActivityIntent = Intent(context, MainActivity::class.java)
//                val pendingIntent = PendingIntent.getActivity(
//                    context,
//                    0,
//                    runMainActivityIntent,
//                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//                )

                notification.apply {
                    val calendar = Calendar.getInstance()
                    val timeString = context.getString(
                        R.string.alarm_content,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                    )
//                    val plusOneMinutePI = PendingIntent.getService(
//                        context,
//                        0,
//                        Intent(context, ClockService::class.java).apply {
//                            putExtra(HOUR, calendar.get(Calendar.HOUR_OF_DAY))
//                            putExtra(MINUTE, calendar.get(Calendar.MINUTE) + 1)
//                        },
//                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//                    )

//                    setCustomBigContentView(
//                        RemoteViews(
//                            context.packageName,
//                            R.layout.layout_notify
//                        ).apply {
//                            setTextViewText(R.id.timer_text, timeString)
//                            setOnClickPendingIntent(
//                                R.id.plus_one_minute_button,
//                                plusOneMinutePI
//                            )
//                        }
//                    )
                    setCustomHeadsUpContentView(
                        RemoteViews(
                            context.packageName,
                            R.layout.layout_notify
                        ).apply {
                            setTextViewText(R.id.timer_text, timeString)
//                            setOnClickPendingIntent(
//                                R.id.plus_one_minute_button,
//                                plusOneMinutePI
//                            )
                        }
                    )
                    setSmallIcon(R.drawable.ic_notify)
                    setContentTitle("Báo thức")
                    setContentText(timeString)
//                    addAction(
//                        R.mipmap.ic_plus_one_minute,
//                        "+1 phút nữa :D",
//                        plusOneMinutePI
//                    )
//                    setContentIntent(pendingIntent)
                    setTimeoutAfter(3000)
                }

                notificationManager.notify(ALARM_ID, notification.build())
            }
            else -> println("Gì? Ai biết gì đâu? :D")
        }
    }
}
