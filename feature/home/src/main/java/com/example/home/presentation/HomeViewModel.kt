package com.example.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.gameState.data.GameState
import com.example.data.gameState.domain.ObserveGameStateUseCase
import com.example.designsystem.resources.ShopItem
import com.example.home.domain.SelectGoalUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    observeGameState: ObserveGameStateUseCase,
    private val selectGoal: SelectGoalUseCase
) : ViewModel() {

    val state: StateFlow<GameState?> = observeGameState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun onGoalPicked(goal: ShopItem) = viewModelScope.launch {
        selectGoal(goal.id)
    }
}