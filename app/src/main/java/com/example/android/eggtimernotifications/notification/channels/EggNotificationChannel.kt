package com.example.android.eggtimernotifications.notification.channels

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.android.eggtimernotifications.R

object EggNotificationChannel {
    const val ID = R.string.egg_notification_channel_id
    private const val NAME = R.string.egg_notification_channel_name
    private const val IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

    @RequiresApi(Build.VERSION_CODES.O)
    fun create(context: Context): NotificationChannel {
        return NotificationChannel(
            context.getString(ID),
            context.getString(NAME),
            IMPORTANCE,
        )
            .apply {
                description = context.getString(R.string.breakfast_notification_channel_description)
                lightColor = Color.RED
                enableLights(true)
                enableVibration(true)
                setShowBadge(false)
            }
    }
}