package com.example.moneyboxgame.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneyboxgame.domain.ObserveGameStateUseCase
import com.example.moneyboxgame.presentation.SplashViewModel
import com.example.onboarding.presentation.OnboardingViewModel
import com.example.onboarding.domain.SavePetNameUseCase
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AppViewModelFactory @Inject constructor(
    private val observeGameState: ObserveGameStateUseCase,
    private val savePetName: SavePetNameUseCase,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SplashViewModel::class.java) ->
            SplashViewModel(observeGameState) as T
        modelClass.isAssignableFrom(OnboardingViewModel::class.java) ->
            OnboardingViewModel(savePetName) as T
        else -> error("Unknown VM: ${modelClass.name}")
    }
}