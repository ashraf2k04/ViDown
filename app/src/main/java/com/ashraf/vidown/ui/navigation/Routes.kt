package com.ashraf.vidown.ui.navigation

import android.net.Uri

sealed class Routes(val route: String) {

    data object HomeScreen : Routes("home")
    data object DownloadsScreen : Routes("download")
    data object PlaylistScreen : Routes("playlist/{url}") {
        fun createRoute(url: String): String =
            "playlist/${Uri.encode(url)}"
    }

}