package com.example.android.eggtimernotifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import com.example.android.eggtimernotifications.notification.channels.EggNotificationChannel

class EggTimerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val eggNotificationChannel = NotificationChannel(
                getString(EggNotificationChannel.ID),
                getString(EggNotificationChannel.NAME),
                EggNotificationChannel.IMPORTANCE
            )
                .apply {
                    description = getString(R.string.breakfast_notification_channel_description)
                    lightColor = Color.RED
                    enableLights(true)
                    enableVibration(true)
                    setShowBadge(false)
                }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannels(listOf(
                eggNotificationChannel
            ))
        }
    }
}