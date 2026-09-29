package com.example.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.gameState.data.OwlAccessory
import com.example.data.gameState.data.OwlEyeColor
import com.example.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.example.onboarding.domain.usecase.SaveOwlAccessoryUseCase
import com.example.onboarding.domain.usecase.SaveOwlEyeColorUseCase
import com.example.onboarding.domain.usecase.SavePetNameUseCase
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val completeOnboarding: CompleteOnboardingUseCase,
    private val savePetName: SavePetNameUseCase,
    private val saveEyeColor: SaveOwlEyeColorUseCase,
    private val saveAccessory: SaveOwlAccessoryUseCase,
) : ViewModel() {

    fun saveName(name: String, onDone: () -> Unit) = viewModelScope.launch {
        savePetName(name)
        onDone()
    }

    fun savePetView(
        color: OwlEyeColor,
        accessory: OwlAccessory?,
        onDone: () -> Unit,
    ) = viewModelScope.launch {
        saveEyeColor(color)
        saveAccessory(accessory)
        completeOnboarding()
        onDone()
    }
}
