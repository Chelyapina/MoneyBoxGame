package com.example.tasks.domain

import com.example.data.gameState.data.GameStateStore
import com.example.data.gameState.data.TaskReward
import javax.inject.Inject

class ApplyTaskRewardUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    suspend operator fun invoke(reward: TaskReward) {
        store.applyTaskReward(
            foodCoins = reward.foodCoins,
            funCoins = reward.funCoins,
            satietyCost = reward.satietyCost,
        )
    }
}

