package com.example.data.gameState.data

enum class OwlSize { SMALL, MEDIUM, LARGE }
enum class OwlMood { BAD, NEUTRAL, GOOD }
enum class OwlAccessory { HAT, CAP, GRADUATION_CAP, CROWN }
enum class OwlEyeColor { BLUE, YELLOW, VIOLET, GREEN }

data class GameState(
    val petName: String? = null,
    val isOnboardingDone: Boolean = false,

    val level: Int = 1,
    val goalId: String? = null,

    val owlMood: OwlMood = OwlMood.NEUTRAL,
    val owlAccessory: OwlAccessory? = null,
    val owlEyeColor: OwlEyeColor = OwlEyeColor.YELLOW,

    val satiety: Int = 10,
    val balanceFood: Int = 25,
    val balanceFun: Int = 25,
    val savings: Int = 0,

    val purchasesThisLevel: List<String> = emptyList(),
    val planChanges: Int = 0,
    val planFood: Int = 0,
    val planFun: Int = 0,
    val planSavings: Int = 0,

    val spentFood: Int = 0,
    val spentFun: Int = 0,
) {
    val owlSize: OwlSize
        get() = when {
            level <= 1 -> OwlSize.SMALL
            level <= 2 -> OwlSize.MEDIUM
            else -> OwlSize.LARGE
        }

    val coins: Int
        get() = balanceFood + balanceFun

    companion object {
        val Empty = GameState()
    }
}