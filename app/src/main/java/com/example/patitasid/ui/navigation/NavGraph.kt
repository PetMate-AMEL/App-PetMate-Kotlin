package com.example.patitasid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.patitasid.domain.model.StartDestination
import com.example.patitasid.ui.feature.home.HomeScreen
import com.example.patitasid.ui.feature.password.ForgotPasswordScreen
import com.example.patitasid.ui.feature.password.ResetPasswordScreen
import com.example.patitasid.ui.feature.pets.BusinessPanelScreen
import com.example.patitasid.ui.feature.pets.DemoScreen
import com.example.patitasid.ui.feature.pets.PetsScreen
import com.example.patitasid.ui.feature.signup.SignUpScreen
import com.example.patitasid.ui.feature.startup.StartupScreen
import com.example.patitasid.ui.feature.startup.StartupViewModel
import com.example.patitasid.ui.feature.welcome.WelcomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavGraph() {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = StartupRoute) {
        composable<StartupRoute> {
            StartupDestination(controller = controller)
        }
        composable<WelcomeRoute> {
            WelcomeScreen(
                onLogin = { controller.navigate(LoginRoute) },
                onSignUp = { controller.navigate(SignUpRoute) },
                onTryDemo = { controller.navigate(DemoRoute) },
                onForgotPassword = { controller.navigate(ForgotPasswordRoute) }
            )
        }
        composable<SignUpRoute> {
            SignUpScreen(
                onBack = { controller.popBackStack() },
                // The welcome screen must not come back with the back button once there is a session.
                onAccountCreated = {
                    controller.navigate(PetsRoute) { popUpTo<WelcomeRoute> { inclusive = true } }
                }
            )
        }
        // Placeholder until HU-010 builds the login screen.
        composable<LoginRoute> {
            HomeScreen(onBack = { controller.popBackStack() })
        }
        composable<DemoRoute> {
            DemoScreen(onBack = { controller.popBackStack() })
        }
        composable<PetsRoute> {
            PetsScreen(onBack = { controller.popBackStack() })
        }
        composable<BusinessPanelRoute> {
            BusinessPanelScreen(onBack = { controller.popBackStack() })
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

// Reads the saved session and replaces itself with the right first screen (RF-01, RF-57).
@Composable
private fun StartupDestination(controller: NavHostController, viewModel: StartupViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    StartupScreen()
    LaunchedEffect(state.destination) {
        val destination = state.destination ?: return@LaunchedEffect
        val route: Any = when (destination) {
            StartDestination.Welcome -> WelcomeRoute
            StartDestination.Pets -> PetsRoute
            StartDestination.BusinessPanel -> BusinessPanelRoute
        }
        controller.navigate(route) { popUpTo<StartupRoute> { inclusive = true } }
    }
}
