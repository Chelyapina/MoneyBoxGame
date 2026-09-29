package com.example.data.gameState.domain

import com.example.data.gameState.data.GameState
import com.example.data.gameState.data.GameStateStore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveGameStateUseCase @Inject constructor(
    private val store: GameStateStore,
) {
    operator fun invoke(): Flow<GameState> = store.state
}