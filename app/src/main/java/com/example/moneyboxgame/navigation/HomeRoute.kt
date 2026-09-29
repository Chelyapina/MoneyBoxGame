package com.example.moneyboxgame.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.home.presentation.HomeScreen
import com.example.home.presentation.HomeViewModel
import com.example.savings.domain.SavingsScreen
import com.example.savings.SavingsViewModel

@Composable
fun HomeRoute(
    vmFactory: ViewModelProvider.Factory,
    currentRoute: String?,
    onTabClick: (String) -> Unit,
    onSavingsClick: () -> Unit,
    onSatietyClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val vm: HomeViewModel = viewModel(factory = vmFactory)
    val state by vm.state.collectAsStateWithLifecycle()

    state?.let { real ->
        HomeScreen(
            state = real,
            currentRoute = currentRoute,
            onGoalPicked = vm::onGoalPicked,
            onTabClick = onTabClick,
            onSavingsClick = onSavingsClick,
            onSatietyClick = onSatietyClick,
            onSettingsClick = onSettingsClick,
        )
    }
}