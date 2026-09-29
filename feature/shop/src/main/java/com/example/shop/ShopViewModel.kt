package com.example.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.gameState.data.GameState
import com.example.data.gameState.data.PurchaseCalculator
import com.example.data.gameState.data.PurchaseOutcome
import com.example.data.gameState.domain.ObserveGameStateUseCase
import com.example.designsystem.resources.ShopItem
import com.example.shop.domain.PurchaseItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShopViewModel(
    observeGameState: ObserveGameStateUseCase,
    private val purchaseItem: PurchaseItemUseCase,
) : ViewModel() {

    val state: StateFlow<GameState?> = observeGameState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _dialogState = MutableStateFlow<ShopDialogState?>(null)
    val dialogState: StateFlow<ShopDialogState?> = _dialogState

    fun onItemClick(item: ShopItem) {
        val current = state.value ?: return
        val outcome = PurchaseCalculator.calculate(current, item)
        _dialogState.value = ShopDialogState(
            item = item,
            outcome = outcome,
            satietyBefore = current.satiety,
        )
    }

    fun onDialogDismiss() {
        _dialogState.value = null
    }

    fun onConfirmPurchase() {
        val dialog = _dialogState.value ?: return
        val current = state.value ?: return
        viewModelScope.launch {
            purchaseItem(current, dialog.item)
            _dialogState.value = null
        }
    }
}

data class ShopDialogState(
    val item: ShopItem,
    val outcome: PurchaseOutcome,
    val satietyBefore: Int,
)