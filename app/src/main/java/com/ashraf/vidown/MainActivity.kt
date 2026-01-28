package com.ashraf.vidown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.ashraf.vidown.ui.screens.mainscreen.MainScreen
import com.ashraf.vidown.ui.theme.VidownTheme
import com.ashraf.vidown.util.PermissionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionManager: PermissionManager

    private val mediaPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissionManager = PermissionManager(
            activity = this,
            mediaLauncher = mediaPermissionLauncher,
            notificationLauncher = notificationPermissionLauncher
        )

        setContent {
            VidownTheme {
                MainScreen()
            }
        }
    }

    // Called from UI when download starts
    fun ensureDownloadPermissions() {
        permissionManager.requestMediaPermission()
        permissionManager.requestNotificationPermission()
    }
}
