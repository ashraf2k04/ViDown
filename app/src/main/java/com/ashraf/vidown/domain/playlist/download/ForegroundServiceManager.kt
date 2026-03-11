package com.ashraf.vidown.domain.playlist.download

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.ashraf.vidown.service.DownloadServiceHolder
import com.ashraf.vidown.service.DownloadService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForegroundServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun start() {
        val serviceIntent =
            Intent(context, DownloadService::class.java)

        ContextCompat.startForegroundService(
            context,
            serviceIntent
        )
    }

    suspend fun awaitService(): DownloadService? {
        var retries = 0
        while (DownloadServiceHolder.service == null && retries < 40) {
            delay(50)
            retries++
        }
        return DownloadServiceHolder.service
    }
}