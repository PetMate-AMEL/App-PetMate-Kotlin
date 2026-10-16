package com.example.patitasid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.patitasid.ui.feature.home.HomeScreen
import com.example.patitasid.ui.feature.welcome.WelcomeScreen

@Composable
fun NavGraph() {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = WelcomeRoute) {
        composable<WelcomeRoute> {
            WelcomeScreen(onStart = { controller.navigate(HomeRoute) })
        }
        composable<HomeRoute> {
            HomeScreen(onBack = { controller.popBackStack() })
        }
    }
}
