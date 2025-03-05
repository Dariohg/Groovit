// core/navigation/NavigationWrapper.kt
package com.example.groovit.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.groovit.eventdetail.presentation.EventDetailScreen
import com.example.groovit.eventdetail.presentation.EventDetailViewModel
import com.example.groovit.home.presentation.HomeScreen
import com.example.groovit.home.presentation.HomeViewModel
import com.example.groovit.login.presentation.LoginScreen
import com.example.groovit.login.presentation.LoginViewModel
import com.example.groovit.register.presentation.RegisterScreen
import com.example.groovit.register.presentation.RegisterViewModel

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.LOGIN) {
        composable(route = Screens.LOGIN) {
            LoginScreen(
                loginViewModel = LoginViewModel(),
                navigateToHome = {
                    // Navegación con reemplazo para que no pueda volver al login
                    navController.navigate(Screens.HOME) {
                        popUpTo(Screens.LOGIN) { inclusive = true }
                    }
                },
                navigateToRegister = { navController.navigate(Screens.REGISTER) }
            )
        }

        composable(route = Screens.REGISTER) {
            RegisterScreen(
                registerViewModel = RegisterViewModel(),
                navigateToLogin = { navController.popBackStack() }
            )
        }

        composable(route = Screens.HOME) {
            HomeScreen(
                homeViewModel = HomeViewModel(),
                navigateToEventDetail = { eventId ->
                    navController.navigate("${Screens.EVENT_DETAIL}/$eventId")
                }
            )
        }

        composable(
            route = "${Screens.EVENT_DETAIL}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: "1" // Valor por defecto "1"

            // Crear un ViewModel con una implementación más simple para pruebas
            val viewModel = EventDetailViewModel()

            // Cargar manualmente el evento para probar
            LaunchedEffect(eventId) {
                viewModel.loadEvent(eventId)
            }

            EventDetailScreen(
                eventDetailViewModel = viewModel,
                eventId = eventId,
                navigateBack = { navController.popBackStack() },
                navigateToHome = { navController.popBackStack(Screens.HOME, inclusive = false) }
            )
        }
    }
}