package com.example.moneyboxgame.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.gameState.domain.ObserveGameStateUseCase
import com.example.home.domain.SelectGoalUseCase
import com.example.home.presentation.HomeViewModel
import com.example.moneyboxgame.presentation.SplashViewModel
import com.example.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.example.onboarding.domain.usecase.SaveOwlAccessoryUseCase
import com.example.onboarding.domain.usecase.SaveOwlEyeColorUseCase
import com.example.onboarding.domain.usecase.SavePetNameUseCase
import com.example.onboarding.presentation.OnboardingViewModel
import com.example.savings.SavePlanUseCase
import com.example.savings.SavingsViewModel
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AppViewModelFactory @Inject constructor(
    private val observeGameState: ObserveGameStateUseCase,
    private val savePetName: SavePetNameUseCase,
    private val saveEyeColor: SaveOwlEyeColorUseCase,
    private val saveAccessory: SaveOwlAccessoryUseCase,
    private val selectGoalUseCase: SelectGoalUseCase,
    private val savePlanUseCase: SavePlanUseCase,
    private val completeOnboarding: CompleteOnboardingUseCase,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SplashViewModel::class.java) ->
            SplashViewModel(observeGameState) as T
        modelClass.isAssignableFrom(OnboardingViewModel::class.java) ->
            OnboardingViewModel(
                completeOnboarding,
                savePetName,
                saveEyeColor,
                saveAccessory,
            ) as T
        modelClass.isAssignableFrom(HomeViewModel::class.java) ->
            HomeViewModel(observeGameState, selectGoalUseCase) as T
        modelClass.isAssignableFrom(SavingsViewModel::class.java) ->
            SavingsViewModel(observeGameState, savePlanUseCase) as T
        else -> error("Unknown VM: ${modelClass.name}")
    }
}