package com.example.moneyboxgame.domain

import com.example.data.GameState
import com.example.data.GameStateStore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveGameStateUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    operator fun invoke(): Flow<GameState> = store.state
}