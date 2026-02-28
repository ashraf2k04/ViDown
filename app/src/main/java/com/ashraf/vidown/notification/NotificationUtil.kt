package com.ashraf.vidown.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationUtil {

    fun createChannels(context: Context) {

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        val serviceChannel =
            NotificationChannel(
                SERVICE_CHANNEL_ID,
                "Downloads",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Foreground download service"
            }

        manager.createNotificationChannel(serviceChannel)
    }
}
