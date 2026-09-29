package com.example.home.domain

import com.example.data.gameState.data.GameStateStore
import javax.inject.Inject

class SelectGoalUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    suspend operator fun invoke(goalId: String) {
        store.setGoal(goalId)
    }
}