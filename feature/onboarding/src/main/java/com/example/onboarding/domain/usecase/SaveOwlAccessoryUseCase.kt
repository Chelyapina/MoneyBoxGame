package com.example.onboarding.domain.usecase

import com.example.data.GameStateStore
import com.example.data.OwlAccessory
import javax.inject.Inject

class SaveOwlAccessoryUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    suspend operator fun invoke(accessory: OwlAccessory?) {
        if (accessory != null) store.setOwlAccessory(accessory)
    }
}