package com.ashraf.vidown.domain.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ashraf.vidown.domain.DownloadServiceHolder
import com.ashraf.vidown.notification.NotificationController
import com.ashraf.vidown.notification.SERVICE_NOTIFICATION_ID

class DownloadService : Service() {

    private lateinit var notifier: NotificationController


    override fun onCreate() {
        super.onCreate()

        Log.d("DL_FLOW", "DownloadService.onCreate()")

        notifier = NotificationController(this)
        DownloadServiceHolder.service = this

        startForeground(
            SERVICE_NOTIFICATION_ID,
            notifier.foregroundNotification()
        )
    }

    fun updateProgress(progress: Int, text: String) {
        notifier.updateProgress(progress, text)
    }

    fun complete(title: String) {
        notifier.complete(title)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    fun error(message: String) {
        notifier.error(message)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        DownloadServiceHolder.service = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
