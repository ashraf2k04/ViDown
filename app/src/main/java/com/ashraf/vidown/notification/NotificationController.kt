package com.ashraf.vidown.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ashraf.vidown.MainActivity
import com.ashraf.vidown.R


class NotificationController(
    private val context: Context
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val builder: NotificationCompat.Builder by lazy {
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )

        NotificationCompat.Builder(context, SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle("Downloading")
            .setContentText("Starting…")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(100, 0, true)
            .setContentIntent(pendingIntent)
    }

    fun foregroundNotification(): Notification =
        builder.build()

    fun updateProgress(progress: Int, text: String) {
        builder
            .setProgress(100, progress, false)
            .setContentText(text)

        notificationManager.notify(
            SERVICE_NOTIFICATION_ID,
            builder.build()
        )
    }

    fun complete(title: String) {
        builder
            .setProgress(0, 0, false)
            .setContentTitle("Download complete")
            .setContentText(title)
            .setOngoing(false)

        notificationManager.notify(
            SERVICE_NOTIFICATION_ID,
            builder.build()
        )
    }

    fun error(message: String) {
        builder
            .setProgress(0, 0, false)
            .setContentTitle("Download failed")
            .setContentText(message)
            .setOngoing(false)

        notificationManager.notify(
            SERVICE_NOTIFICATION_ID,
            builder.build()
        )
    }
}