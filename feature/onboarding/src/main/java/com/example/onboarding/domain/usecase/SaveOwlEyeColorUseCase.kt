package com.example.onboarding.domain.usecase

import com.example.data.gameState.data.GameStateStore
import com.example.data.gameState.data.OwlEyeColor
import javax.inject.Inject

class SaveOwlEyeColorUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    suspend operator fun invoke(color: OwlEyeColor) = store.setOwlEyeColor(color)
}