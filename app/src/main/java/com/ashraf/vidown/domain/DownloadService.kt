package com.ashraf.vidown.domain

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.ashraf.vidown.App.Companion.context
import com.ashraf.vidown.MainActivity
import com.ashraf.vidown.notification.NotificationUtil
import com.ashraf.vidown.notification.SERVICE_NOTIFICATION_ID

class DownloadService : Service() {

    override fun onBind(intent: Intent): IBinder {
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )

        startForeground(
            SERVICE_NOTIFICATION_ID,
            NotificationUtil.makeServiceNotification( context, pendingIntent)
        )

        return DownloadServiceBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        return super.onUnbind(intent)
    }

    inner class DownloadServiceBinder : Binder()
}