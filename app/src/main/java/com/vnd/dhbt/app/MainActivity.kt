package com.vnd.dhbt.app

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.MediaController
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.vnd.dhbt.*
import com.vnd.dhbt.base.local.AppSharedPreferences
import com.vnd.dhbt.base.models.TimeAlarm
import com.vnd.dhbt.base.services.AlarmService
import com.vnd.dhbt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val alarmAdapter = AlarmAdapter()
    private lateinit var alarmList: MutableList<TimeAlarm>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        mainFlow()
        playVideo()
    }

    private fun playVideo() {
        binding.videoView.apply {
            val url = "https://dl.dropboxusercontent.com/s/mb2206chn3ucn42/thayba.mp4?dl=0"
            setVideoURI(Uri.parse(url))

            val mediaController = MediaController(this@MainActivity).apply {
                setAnchorView(binding.videoView)
                setMediaPlayer(binding.videoView)
            }

            setMediaController(mediaController)

            start()
        }

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

        // UI
        binding.apply {
            timePicker.setIs24HourView(true)

            setAlarmButton.setOnClickListener {
                val newAlarm = TimeAlarm(
                    id = alarmList.size,
                    hour = timePicker.hour,
                    minute = timePicker.minute,
                    dayOfWeek = daysOfWeek.getValue()
                )

                if (newAlarm.dayOfWeek.isBlank()) {
                    AlertDialog.Builder(this@MainActivity).apply {
                        setTitle("Chưa chọn ngày cần báo thức!")
                        setMessage("Hãy cho chúng tôi biết ngày cần báo thức bằng cách nhấp chọn ngày nhé!")
                        setPositiveButton(
                            "Ok! :D",
                            DialogInterface.OnClickListener { _, _ -> }
                        )
                        setCancelable(false)
                        create()
                        show()
                    }
                } else {
                    AlarmService.setAlarm(this@MainActivity, newAlarm)
                    alarmList.add(newAlarm)
                    alarmAdapter.setData(alarmList)
                    sharedPreferences.saveData(convertDataToString(alarmList))
                }
            }

            alarmContainer.adapter = alarmAdapter.apply {
                setData(alarmList)
                setListener(object : AlarmAdapter.AlarmListener {
                    override fun onClickRemove(position: Int) {
                        alarmContainer.removeViewAt(position)
                        AlarmService.cancelAlarm(this@MainActivity, alarmList[position].id)
                        alarmList.removeAt(position)
                        setData(alarmList)
                        sharedPreferences.saveData(convertDataToString(alarmList))
                    }

                    override fun onSwitched(position: Int, value: Boolean) {
                        alarmList[position].status = value

                        if(alarmList[position].status) AlarmService.setAlarm(
                            this@MainActivity,
                            alarmList[position]
                        )
                        else AlarmService.cancelAlarm(this@MainActivity, alarmList[position].id)

                        sharedPreferences.saveData(convertDataToString(alarmList))
                    }
                })
            }
        }
    }
}
