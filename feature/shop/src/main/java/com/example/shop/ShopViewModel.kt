package com.example.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.gameState.data.GameState
import com.example.data.gameState.domain.ObserveGameStateUseCase
import com.example.designsystem.background.OwlPatternLayer
import com.example.designsystem.components.InformCard
import com.example.designsystem.resources.ItemCategory
import com.example.designsystem.resources.ShopItem
import com.example.designsystem.resources.ShopItems
import com.example.designsystem.theme.OrangeBgBottom
import com.example.designsystem.theme.OrangeBgCenter
import com.example.designsystem.theme.OrangeBgTop
import com.example.designsystem.util.modalBlock
import com.example.shop.domain.PurchaseCalculator
import com.example.shop.domain.PurchaseItemUseCase
import com.example.shop.domain.PurchaseOutcome
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