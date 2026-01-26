package com.ashraf.vidown.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class PermissionManager(
    private val activity: Activity,
    private val mediaLauncher: ActivityResultLauncher<String>,
    private val notificationLauncher: ActivityResultLauncher<String>
) {

    fun hasMediaPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestMediaPermission() {
        if (!hasMediaPermission()) {
            mediaLauncher.launch(
                if (Build.VERSION.SDK_INT >= 33)
                    Manifest.permission.READ_MEDIA_VIDEO
                else
                    Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    fun hasNotificationPermission(): Boolean {
        return Build.VERSION.SDK_INT < 33 ||
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33 && !hasNotificationPermission()) {
            notificationLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }
}
