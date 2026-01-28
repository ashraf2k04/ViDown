package com.ashraf.vidown.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.ashraf.vidown.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val appContext = context.applicationContext

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showProgress(
        notificationId: Int,
        title: String,
        progress: Int,
        text: String? = null
    ) {
        val notification =
            NotificationCompat.Builder(appContext, DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setProgress(100, progress, progress <= 0)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .build()

        notificationManager.notify(notificationId, notification)
    }

    fun showCompleted(
        notificationId: Int,
        title: String,
        text: String? = null,
        intent: PendingIntent? = null
    ) {
        val notification =
            NotificationCompat.Builder(appContext, DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(intent)
                .build()

        notificationManager.notify(notificationId, notification)
    }

    fun showError(
        notificationId: Int,
        title: String,
        text: String
    ) {
        val notification =
            NotificationCompat.Builder(appContext, DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .build()

        notificationManager.notify(notificationId, notification)
    }

    fun cancel(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    fun cancelAll() {
        notificationManager.cancelAll()
    }
}