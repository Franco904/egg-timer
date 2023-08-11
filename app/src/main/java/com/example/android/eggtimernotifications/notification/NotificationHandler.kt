package com.example.android.eggtimernotifications.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.notification.channels.EggNotificationChannel
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver
import com.example.android.eggtimernotifications.ui.activity.MainActivity

class NotificationHandler(
    private val applicationContext: Context,
    private val notificationManager: NotificationManager,
) {
    /**
     * Builds and delivers the notification.
     */
    fun sendNotification(messageBody: String) {
        // Launch activity as the notification content intent callback
        val contentIntent = Intent(applicationContext, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Customize notification style
        val eggImage = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.cooked_egg)
        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(eggImage)

        // Launch broadcast receiver as notification action 1 intent callback
        val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
        val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            REQUEST_CODE,
            snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        // Build the notification
        val notification = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(EggNotificationChannel.ID)
        )
            .setSmallIcon(R.drawable.cooked_egg)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)
            // Will open MainActivity when content is clicked
            .setContentIntent(contentPendingIntent)
            // Dismiss notification when content is clicked
            .setAutoCancel(true)
            // Notification style
            .setStyle(bigPicStyle)
            .setLargeIcon(eggImage)
            // Snooze action
            .addAction(
                R.drawable.egg_icon,
                applicationContext.getString(R.string.snooze),
                snoozePendingIntent
            )
            /**
             * Android Notification Importances/Priorities:
             *
             * High: Makes a sound and appears as heads-up notification
             * Default: Makes a sound
             * Low: No sound
             * Min: No sound and does not appear on status bar
             */
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        try {
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(NOTIFICATION_ID, notification)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    /**
     * Cancels all notifications
     */
    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val REQUEST_CODE = 0
    }
}