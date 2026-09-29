package com.example.moneyboxgame.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.savings.SavingsViewModel
import com.example.savings.domain.SavingsScreen

@Composable
fun SavingsRoute(
    vmFactory: ViewModelProvider.Factory,
    onBack: () -> Unit,
    onPlanSaved: () -> Unit = onBack,
) {
    val vm: SavingsViewModel = viewModel(factory = vmFactory)
    val state by vm.state.collectAsStateWithLifecycle()

    state?.let { real ->
        SavingsScreen(
            state = real,
            onBack = onBack,
            onPlanConfirmed = { food, funAmount, savings ->
                vm.onPlanConfirmed(food, funAmount, savings)
                onPlanSaved()
            },
        )
    }
}