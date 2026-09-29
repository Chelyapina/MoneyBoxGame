package com.example.savings

import com.example.data.gameState.data.GameStateStore
import javax.inject.Inject

class SavePlanUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    suspend operator fun invoke(food: Int, funAmount: Int, savings: Int) {
        require(food >= 0 && funAmount >= 0 && savings >= 0) {
            "Plan values must be non-negative"
        }
        store.setPlan(food, funAmount, savings)
    }
}