package com.example.patitasid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.example.patitasid.ui.feature.home.HomeScreen
import com.example.patitasid.ui.feature.password.ForgotPasswordScreen
import com.example.patitasid.ui.feature.password.ResetPasswordScreen
import com.example.patitasid.ui.feature.welcome.WelcomeScreen

@Composable
fun NavGraph() {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = WelcomeRoute) {
        composable<WelcomeRoute> {
            WelcomeScreen(
                onStart = { controller.navigate(HomeRoute) },
                onForgotPassword = { controller.navigate(ForgotPasswordRoute) }
            )
        }
        composable<HomeRoute> {
            HomeScreen(onBack = { controller.popBackStack() })
        }
        composable<ForgotPasswordRoute> {
            ForgotPasswordScreen(onBack = { controller.popBackStack() })
        }
        composable<ResetPasswordRoute>(
            deepLinks = listOf(navDeepLink<ResetPasswordRoute>(basePath = RESET_PASSWORD_DEEP_LINK))
        ) { entry ->
            ResetPasswordScreen(
                token = entry.toRoute<ResetPasswordRoute>().token,
                onBack = { controller.popBackStack() },
                onRequestNewLink = {
                    controller.navigate(ForgotPasswordRoute) { popUpTo<ResetPasswordRoute> { inclusive = true } }
                },
                // Login does not exist yet: go back to the start until its HU lands, then use LoginRoute.
                onGoToLogin = {
                    controller.navigate(WelcomeRoute) { popUpTo<WelcomeRoute> { inclusive = true } }
                }
            )
        }
    }
}
