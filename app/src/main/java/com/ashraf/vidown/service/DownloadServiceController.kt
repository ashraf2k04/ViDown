package com.ashraf.vidown.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadServiceController @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun ensureServiceReady(): DownloadService? {

        val intent = Intent(context, DownloadService::class.java)
        ContextCompat.startForegroundService(context, intent)

        var retries = 0
        while (DownloadServiceHolder.service == null && retries < 40) {
            delay(50)
            retries++
        }

        return DownloadServiceHolder.service
    }
}