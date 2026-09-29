package com.example.tasks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.gameState.data.TaskReward
import com.example.tasks.domain.ApplyTaskRewardUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val applyReward: ApplyTaskRewardUseCase,
) : ViewModel() {

    fun onTaskCompleted(reward: TaskReward) = viewModelScope.launch {
        applyReward(reward)
    }
}