package com.example.onboarding.domain.usecase

import com.example.data.GameStateStore
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    suspend operator fun invoke() = store.completeOnboarding()
}