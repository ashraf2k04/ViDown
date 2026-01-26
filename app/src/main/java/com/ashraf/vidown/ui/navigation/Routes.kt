package com.ashraf.vidown.ui.navigation

sealed class Routes(val route: String) {

    data object HomeScreen : Routes("home")
    data object DownloadsScreen : Routes("download")

}