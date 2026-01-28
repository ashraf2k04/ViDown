package com.ashraf.vidown.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.ashraf.vidown.R

object NotificationUtil {

    fun createChannels(context: Context) {

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val downloadChannel = NotificationChannel(
            DOWNLOAD_CHANNEL_ID,
            "Downloads",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Download progress"
        }

        val serviceChannel = NotificationChannel(
            SERVICE_CHANNEL_ID,
            "Download Service",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Foreground download service"
        }

        manager.createNotificationChannel(downloadChannel)
        manager.createNotificationChannel(serviceChannel)
    }

    fun makeServiceNotification(
        context: Context,
        contentIntent: PendingIntent,
        text: String = "Downloading…"
    ): Notification {
        return NotificationCompat.Builder(context, SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(text)
            .setOngoing(true)
            .setContentIntent(contentIntent)
            .build()
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)
    }

    fun cancelAll(context: Context) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
    }
}