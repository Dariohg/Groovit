package com.example.groovit.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                    // Aquí irá la navegación al detalle del evento cuando se implemente
                    // navController.navigate("${Screens.EVENT_DETAIL}/$eventId")
                }
            )
        }
    }
}