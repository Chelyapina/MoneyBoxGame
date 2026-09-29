package com.example.shop.domain

import com.example.data.gameState.data.GameState
import com.example.data.gameState.data.GameStateStore
import com.example.data.gameState.data.PurchaseCalculator
import com.example.data.gameState.data.PurchaseOutcome
import com.example.designsystem.resources.ShopItem
import javax.inject.Inject

class PurchaseItemUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    suspend operator fun invoke(
        current: GameState,
        item: ShopItem,
    ): PurchaseOutcome {
        val outcome = PurchaseCalculator.calculate(current, item)
        if (outcome is PurchaseOutcome.Success) {
            store.applyPurchase(
                itemId = outcome.itemId,
                balanceFood = outcome.newBalanceFood,
                balanceFun = outcome.newBalanceFun,
                satiety = outcome.newSatiety,
                owlMood = outcome.newMood,
                level = outcome.newLevel,
                clearGoal = outcome.clearGoal,
            )
        }
        return outcome
    }
}