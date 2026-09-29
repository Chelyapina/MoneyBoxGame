package com.example.shop.domain

import com.example.data.gameState.data.GameState
import com.example.data.gameState.data.OwlMood
import com.example.designsystem.resources.ItemCategory
import com.example.designsystem.resources.ShopItem


sealed interface PurchaseOutcome {
    data class Success(
        val newBalanceFood: Int,
        val newBalanceFun: Int,
        val newSatiety: Int,
        val newMood: OwlMood,
        val newLevel: Int,
        val leveledUp: Boolean,
        val clearGoal: Boolean,
        val itemId: String,
    ) : PurchaseOutcome

    data class NotEnoughMoney(val shortage: Int) : PurchaseOutcome
    data object AlreadyFull : PurchaseOutcome
    data object CannotImproveMood : PurchaseOutcome

    data class GoalInProgress(
        val saved: Int,
        val price: Int,
    ) : PurchaseOutcome
    data object GoalAlreadyChosen : PurchaseOutcome
    data object NoGoalChosen : PurchaseOutcome
}

object PurchaseCalculator {

    private const val MAX_SATIETY = 10

    fun calculate(current: GameState, item: ShopItem): PurchaseOutcome {
        if (item.category == ItemCategory.GOAL) return calculateGoal(current, item)

        if (current.coins < item.price) {
            return PurchaseOutcome.NotEnoughMoney(item.price - current.coins)
        }

        return when (item.category) {
            ItemCategory.MANDATORY -> {
                if (current.satiety >= MAX_SATIETY) {
                    PurchaseOutcome.AlreadyFull
                } else {
                    val raw = current.satiety + (item.effect ?: 0)
                    val leveledUp = raw >= MAX_SATIETY
                    val newSatiety = if (leveledUp) raw - MAX_SATIETY else raw

                    PurchaseOutcome.Success(
                        newBalanceFood = current.balanceFood - item.price,
                        newBalanceFun = current.balanceFun,
                        newSatiety = newSatiety,
                        newMood = moodFromSatiety(newSatiety),
                        newLevel = if (leveledUp) current.level + 1 else current.level,
                        leveledUp = leveledUp,
                        clearGoal = leveledUp,
                        itemId = item.id,
                    )
                }
            }

            ItemCategory.OPTIONAL -> {
                if (current.owlMood == OwlMood.GOOD) {
                    PurchaseOutcome.CannotImproveMood
                } else {
                    PurchaseOutcome.Success(
                        newBalanceFood = current.balanceFood,
                        newBalanceFun = current.balanceFun - item.price,
                        newSatiety = current.satiety,
                        newMood = nextMood(current.owlMood),
                        newLevel = current.level,
                        leveledUp = false,
                        clearGoal = false,
                        itemId = item.id,
                    )
                }
            }

            ItemCategory.GOAL -> error("unreachable")
        }
    }

    private fun calculateGoal(current: GameState, item: ShopItem): PurchaseOutcome {
        val currentGoalId = current.goalId
            ?: return PurchaseOutcome.NoGoalChosen

        return if (currentGoalId == item.id) {
            PurchaseOutcome.GoalInProgress(
                saved = current.savings,
                price = item.price,
            )
        } else {
            PurchaseOutcome.GoalAlreadyChosen
        }
    }

    fun moodFromSatiety(satiety: Int): OwlMood = when (satiety.coerceIn(0, MAX_SATIETY)) {
        in 0..3 -> OwlMood.BAD
        in 4..6 -> OwlMood.NEUTRAL
        else -> OwlMood.GOOD
    }

    private fun nextMood(mood: OwlMood): OwlMood = when (mood) {
        OwlMood.BAD -> OwlMood.NEUTRAL
        OwlMood.NEUTRAL -> OwlMood.GOOD
        OwlMood.GOOD -> OwlMood.GOOD
    }
}

