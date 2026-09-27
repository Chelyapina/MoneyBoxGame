package com.example.moneyboxgame.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyboxgame.domain.ObserveGameStateUseCase
import com.example.moneyboxgame.navigation.Route
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(observe: ObserveGameStateUseCase) : ViewModel() {

    val startDestination: StateFlow<String?> = observe()
        .map { if (it.isOnboardingDone) Route.HOME else Route.ONBOARDING }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}