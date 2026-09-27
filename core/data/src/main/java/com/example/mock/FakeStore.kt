package com.example.mock

import com.example.data.GameState
import com.example.data.GameStateStore
import com.example.data.OwlAccessory
import com.example.data.OwlEyeColor
import com.example.data.OwlMood
import com.example.data.OwlSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeStore(
    initial: GameState = GameState.Empty,
) : GameStateStore {

    private val _state = MutableStateFlow(initial)
    override val state: StateFlow<GameState> = _state

    var savedName: String? = null
    var savedEyeColor: OwlEyeColor? = null
    var savedAccessory: OwlAccessory? = null
    var savedSize: OwlSize? = null
    var savedMood: OwlMood? = null
    var onboardingCompleted = false

    override suspend fun setPetName(name: String) {
        savedName = name
        _state.value = _state.value.copy(petName = name)
    }

    override suspend fun setOwlEyeColor(color: OwlEyeColor) {
        savedEyeColor = color
        _state.value = _state.value.copy(owlEyeColor = color)
    }

    override suspend fun setOwlAccessory(accessory: OwlAccessory) {
        savedAccessory = accessory
        _state.value = _state.value.copy(owlAccessory = accessory)
    }

    override suspend fun setOwlSize(size: OwlSize) {
        savedSize = size
        _state.value = _state.value.copy(owlSize = size)
    }

    override suspend fun setOwlMood(mood: OwlMood) {
        savedMood = mood
        _state.value = _state.value.copy(owlMood = mood)
    }

    override suspend fun completeOnboarding() {
        onboardingCompleted = true
        _state.value = _state.value.copy(isOnboardingDone = true)
    }
}