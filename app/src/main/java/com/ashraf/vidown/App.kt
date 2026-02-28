package com.ashraf.vidown

import android.app.Application
import android.content.Context
import android.util.Log
import com.ashraf.vidown.notification.NotificationUtil
import com.yausername.aria2c.Aria2c
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NotificationUtil.createChannels(this)

        // 🔹 Required by Downloader
        context = applicationContext
        applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        YoutubeDL.init(this)
        FFmpeg.getInstance().init(this)
        Aria2c.getInstance().init(this)

        Log.d("FLOW", "Version = ${YoutubeDL.getInstance().version(this)}")


// 🔹 Optional: update youtube-dl

        applicationScope.launch {
            try {
                YoutubeDL.getInstance().updateYoutubeDL(this@App)


                val version = YoutubeDL.getInstance().version(this@App)
                Log.d("FLOW", "yt-dlp updated, version = $version")


            } catch (e: Exception) {
                Log.e("FLOW", "yt-dlp update failed", e)
            }
        }

    }

    companion object {
        lateinit var applicationScope: CoroutineScope
        lateinit var context: Context
    }
}