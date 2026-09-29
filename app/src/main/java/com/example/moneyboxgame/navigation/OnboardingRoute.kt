package com.example.moneyboxgame.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onboarding.presentation.OnboardingScreen
import com.example.onboarding.presentation.OnboardingViewModel
import com.example.shop.ShopScreen
import com.example.shop.ShopViewModel

@Composable
fun OnboardingRoute(
    vmFactory: ViewModelProvider.Factory,
    onFinished: () -> Unit,
) {
    val vm: OnboardingViewModel = viewModel(factory = vmFactory)
    OnboardingScreen(
        onNameConfirmed = { name ->
            vm.saveName(name) {  }
        },
        onPetViewConfirmed = { eyes, acc ->
            vm.savePetView(eyes, acc) {
                onFinished()
            }
        },
    )
}