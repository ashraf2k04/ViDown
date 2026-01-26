package com.ashraf.vidown.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ashraf.vidown.ui.screens.downloads.DownloadsScreen
import com.ashraf.vidown.ui.screens.homescreen.HomeScreen


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
    }
}