package com.ashraf.vidown.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ashraf.vidown.ui.screens.downloads.DownloadsScreen
import com.ashraf.vidown.ui.screens.homescreen.HomeScreen
import com.ashraf.vidown.ui.screens.playlist.PlaylistScreen


@Composable
fun NavigationHost(
    modifier : Modifier,
    navController: NavHostController
){

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.HomeScreen.route,
    ){
        composable(Routes.HomeScreen.route) {
            HomeScreen(navController)
        }

        composable(Routes.DownloadsScreen.route) {
            DownloadsScreen()
        }

        composable(
            route = Routes.PlaylistScreen.route,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments!!.getString("url")!!
            PlaylistScreen(url = url, navController = navController)
        }

    }
}