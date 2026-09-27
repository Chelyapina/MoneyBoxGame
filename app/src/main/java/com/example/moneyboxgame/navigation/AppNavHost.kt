package com.example.moneyboxgame.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(startDestination: String, vmFactory: ViewModelProvider.Factory) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = startDestination) {

        composable(Route.ONBOARDING) {
            OnboardingRoute(
                vmFactory = vmFactory,
                onFinished = {
                    nav.navigate(Route.HOME) {
                        popUpTo(Route.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.HOME) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Home stub")
            }
        }
    }
}