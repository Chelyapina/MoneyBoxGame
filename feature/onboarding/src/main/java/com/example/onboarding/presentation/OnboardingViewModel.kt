package com.example.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onboarding.domain.SavePetNameUseCase
import kotlinx.coroutines.launch

class OnboardingViewModel(private val savePetName: SavePetNameUseCase) : ViewModel() {
    fun saveName(name: String, onDone: () -> Unit) = viewModelScope.launch {
        savePetName(name)
        onDone()
    }
}

