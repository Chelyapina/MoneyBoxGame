package com.example.savings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.gameState.data.GameState
import com.example.data.gameState.domain.ObserveGameStateUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavingsViewModel(
    observeGameState: ObserveGameStateUseCase,
    private val savePlan: SavePlanUseCase,
) : ViewModel() {

    val state: StateFlow<GameState?> = observeGameState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun onPlanConfirmed(food: Int, funAmount: Int, savings: Int) =
        viewModelScope.launch {
            savePlan(food, funAmount, savings)
        }
}