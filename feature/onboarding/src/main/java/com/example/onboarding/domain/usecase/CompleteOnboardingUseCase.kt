package com.example.onboarding.domain.usecase

import com.example.data.gameState.data.GameStateStore
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    suspend operator fun invoke() = store.completeOnboarding()
}