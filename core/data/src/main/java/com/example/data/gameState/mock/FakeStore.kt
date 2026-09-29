package com.example.data.gameState.mock

import com.example.data.gameState.data.GameState
import com.example.data.gameState.data.GameStateStore
import com.example.data.gameState.data.OwlAccessory
import com.example.data.gameState.data.OwlEyeColor
import com.example.data.gameState.data.OwlMood
import com.example.data.gameState.data.PurchaseCalculator
import com.example.data.gameState.data.TaskReward
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeStore(
    initial: GameState = GameState.Empty,
) : GameStateStore {

    private val _state = MutableStateFlow(initial)
    override val state: StateFlow<GameState> = _state

    var savedName: String? = null
    var savedEyeColor: OwlEyeColor? = null
    var savedAccessory: OwlAccessory? = null
    var savedMood: OwlMood? = null
    var savedGoalId: String? = null
    var savedPlan: Triple<Int, Int, Int>? = null
    var savedPurchase: SavedPurchase? = null
    var savedTaskReward: TaskReward? = null
    var onboardingCompleted = false

    override suspend fun setPetName(name: String) {
        savedName = name
        _state.value = _state.value.copy(petName = name)
    }

    override suspend fun setOwlEyeColor(color: OwlEyeColor) {
        savedEyeColor = color
        _state.value = _state.value.copy(owlEyeColor = color)
    }

    override suspend fun setOwlAccessory(accessory: OwlAccessory) {
        savedAccessory = accessory
        _state.value = _state.value.copy(owlAccessory = accessory)
    }

    override suspend fun setOwlMood(mood: OwlMood) {
        savedMood = mood
        _state.value = _state.value.copy(owlMood = mood)
    }

    override suspend fun setGoal(goalId: String) {
        savedGoalId = goalId
        _state.value = _state.value.copy(goalId = goalId)
    }

    override suspend fun setPlan(food: Int, funAmount: Int, savings: Int) {
        savedPlan = Triple(food, funAmount, savings)
        _state.value = _state.value.copy(
            planFood = food,
            planFun = funAmount,
            planSavings = savings,
            balanceFood = food,
            balanceFun = funAmount,
            savings = savings,
        )
    }

    override suspend fun applyPurchase(
        itemId: String,
        balanceFood: Int,
        balanceFun: Int,
        satiety: Int,
        owlMood: OwlMood,
        level: Int,
        clearGoal: Boolean,
    ) {
        savedPurchase = SavedPurchase(
            itemId = itemId,
            balanceFood = balanceFood,
            balanceFun = balanceFun,
            satiety = satiety,
            owlMood = owlMood,
            level = level,
            clearGoal = clearGoal,
        )
        _state.value = _state.value.copy(
            balanceFood = balanceFood,
            balanceFun = balanceFun,
            satiety = satiety,
            owlMood = owlMood,
            level = level,
            goalId = if (clearGoal) null else _state.value.goalId,
            purchasesThisLevel = _state.value.purchasesThisLevel + itemId,
        )
    }

    override suspend fun applyTaskReward(
        foodCoins: Int,
        funCoins: Int,
        satietyCost: Int,
    ) {
        savedTaskReward = TaskReward(foodCoins, funCoins, satietyCost)
        val s = _state.value
        val newSatiety = (s.satiety - satietyCost).coerceAtLeast(0)
        _state.value = s.copy(
            balanceFood = s.balanceFood + foodCoins,
            balanceFun = s.balanceFun + funCoins,
            satiety = newSatiety,
            owlMood = PurchaseCalculator.moodFromSatiety(newSatiety),
        )
    }

    override suspend fun completeOnboarding() {
        onboardingCompleted = true
        _state.value = _state.value.copy(isOnboardingDone = true)
    }
}

data class SavedPurchase(
    val itemId: String,
    val balanceFood: Int,
    val balanceFun: Int,
    val satiety: Int,
    val owlMood: OwlMood,
    val level: Int,
    val clearGoal: Boolean,
)