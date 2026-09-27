package com.example.data

enum class OwlSize { SMALL, MEDIUM, LARGE }
enum class OwlMood { BAD, NEUTRAL, GOOD }
enum class OwlAccessory { HAT, CAP, GRADUATION_CAP, CROWN }
enum class OwlEyeColor { BLUE, YELLOW, VIOLET, GREEN }

data class GameState(
    val petName: String? = null,
    val owlSize: OwlSize = OwlSize.SMALL,
    val owlMood: OwlMood = OwlMood.NEUTRAL,
    val owlAccessory: OwlAccessory? = null,
    val owlEyeColor: OwlEyeColor = OwlEyeColor.YELLOW,
    val isOnboardingDone: Boolean = false,
) {

    companion object {
        val Empty = GameState()
    }
}