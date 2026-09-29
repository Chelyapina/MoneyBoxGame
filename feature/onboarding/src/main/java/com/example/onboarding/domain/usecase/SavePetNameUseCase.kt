package com.example.onboarding.domain.usecase

import com.example.data.gameState.data.GameStateStore
import javax.inject.Inject

class SavePetNameUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    suspend operator fun invoke(name: String) {
        val trimmed = name.trim()
        require(trimmed.isNotEmpty())
        store.setPetName(trimmed)
    }
}