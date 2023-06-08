package com.vnd.dhbt

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.vnd.dhbt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val alarmAdapter = AlarmAdapter()
    private lateinit var alarmList: MutableList<TimeAlarm>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainFlow()
    }

    private fun mainFlow() {
        /**
         * PG = permission granted
         */
        val areNotificationsPG = NotificationManagerCompat.from(this).areNotificationsEnabled()
        if (!areNotificationsPG) {
            val requestPermissionIntent =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    }
                } else {
                    Intent(Settings.ACTION_SETTINGS)
                }

            AlertDialog.Builder(this).apply {
                setTitle("Hãy cho phép ứng dụng được gửi thông báo đến bạn!")
                setMessage("Chúng tôi cần quyền này để có thể gửi thông báo đến bạn! Hãy cấp quyền này cho chúng tôi nhé! Xin cảm ơn! :D")
                setPositiveButton(
                    "Đi tới nơi cấp quyền ngay :D",
                    DialogInterface.OnClickListener { _, _ ->
                        startActivity(requestPermissionIntent)
                    }
                )
                setNegativeButton(
                    "Không cấp đấy, làm gì nhau :3",
                    DialogInterface.OnClickListener { _, _ ->
                        finish()
                    }
                )
                setCancelable(false)
                create()
                show()
            }
        }

        // get data from shared preferences file
        val sharedPreferences = AppSharedPreferences(this)
        alarmList = convertStringToData(sharedPreferences.getData()) as MutableList<TimeAlarm>

        // render UI
        binding.apply {
            timePicker.setIs24HourView(true)

            setTimeButton.setOnClickListener {
                alarmList.add(TimeAlarm(alarmList.size, timePicker.hour, timePicker.minute))
                sharedPreferences.saveData(convertDataToString(alarmList))
                alarmAdapter.setData(alarmList)
                startService(Intent(this@MainActivity, AlarmService::class.java))
            }

            alarmContainer.adapter = alarmAdapter.apply {
                setData(alarmList)
                setListener(object : AlarmAdapter.AlarmListener {
                    override fun onClickRemove(position: Int) {
//                        btContainer.removeViewAt(position)
//                        alarmList.removeAt(position)
//
//                        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//                        val myIntent = Intent(this@MainActivity, ClockReceiver::class.java).apply {
//                            action = ALARM_ACTION
//                        }
//                        val pendingIntent = PendingIntent.getBroadcast(
//                            this@MainActivity,
//                            0,
//                            myIntent,
//                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//                        )
//
//                        alarmManager.cancel(pendingIntent)
                    }

                    override fun onSwitched(position: Int, value: Boolean) {
                        alarmList[position].status = value
                        sharedPreferences.saveData(convertDataToString(alarmList))
                        startService(Intent(this@MainActivity, AlarmService::class.java))
                    }
                })
            }
        }
    }
}
