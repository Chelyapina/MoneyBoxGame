package com.example.moneyboxgame.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onboarding.presentation.OnboardingScreen
import com.example.onboarding.presentation.OnboardingViewModel

@Composable
fun OnboardingRoute(
    vmFactory: ViewModelProvider.Factory,
    onFinished: () -> Unit,
) {
    val vm: OnboardingViewModel = viewModel(factory = vmFactory)
    OnboardingScreen(
        onStartClick = { name -> vm.saveName(name, onFinished) }
    )
}