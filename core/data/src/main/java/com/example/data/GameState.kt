package com.example.data

data class GameState(
    val petName: String? = null,
) {
    val isOnboardingDone: Boolean get() = petName != null

    companion object {
        val Empty = GameState()
    }
}