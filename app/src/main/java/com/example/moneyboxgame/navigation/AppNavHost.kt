package com.example.moneyboxgame.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(startDestination: String, vmFactory: ViewModelProvider.Factory) {
    val nav = rememberNavController()
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

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
            HomeRoute(
                vmFactory = vmFactory,
                currentRoute = currentRoute,
                onTabClick = { route -> nav.navigateToTab(route) },
                onSavingsClick = { nav.navigate(Route.SAVINGS) },
                onSatietyClick = { nav.navigate(Route.SHOP) },
                onSettingsClick = { nav.navigate(Route.SETTINGS) },
            )
        }

        composable(Route.SHOP) { ShopScreen() }
        composable(Route.TASKS) { TasksScreen() }
        composable(Route.SETTINGS) { SettingsScreen() }
        composable(Route.SAVINGS) { SavingsScreen() }
    }
}
@Composable
fun ShopScreen() = StubScreen("Магазин")

@Composable
fun TasksScreen() = StubScreen("Задания")

@Composable
fun SettingsScreen() = StubScreen("Настройки")

@Composable
fun SavingsScreen() = StubScreen("Мои монеты")

@Composable
private fun StubScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
    }
}

private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(Route.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}