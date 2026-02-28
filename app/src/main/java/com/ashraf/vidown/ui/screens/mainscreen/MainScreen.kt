package com.ashraf.vidown.ui.screens.mainscreen


import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ashraf.vidown.ui.navigation.NavigationHost
import com.ashraf.vidown.ui.navigation.Routes
import com.ashraf.vidown.ui.screens.mainscreen.componets.BottomNavBar
import com.ashraf.vidown.ui.screens.mainscreen.componets.TopBar


@Composable
fun MainScreen() {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val isPlaylist =
        currentRoute?.startsWith(Routes.PlaylistScreen.route) == true

    val showBottomBar = !isPlaylist

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.navigationBars.asPaddingValues()),

        topBar = {
            TopBar(
                title = if (isPlaylist) "Playlist Info" else "Vidown",
                showBack = isPlaylist,
                onBack = { navController.popBackStack() }
            )
        },

        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    navController = navController,
                    scaffoldRoute = currentRoute
                )
            }
        }
    ){ innerPadding ->
            NavigationHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
    }
}

