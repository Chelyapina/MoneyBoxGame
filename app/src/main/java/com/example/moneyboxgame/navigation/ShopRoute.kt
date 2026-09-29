package com.example.moneyboxgame.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.ShopScreen
import com.example.shop.ShopViewModel

@Composable
fun ShopRoute(
    vmFactory: ViewModelProvider.Factory,
    onBack: () -> Unit,
) {
    val vm: ShopViewModel = viewModel(factory = vmFactory)
    val state by vm.state.collectAsStateWithLifecycle()
    val dialog by vm.dialogState.collectAsStateWithLifecycle()

    state?.let { real ->
        ShopScreen(
            state = real,
            dialogState = dialog,
            onBack = onBack,
            onItemClick = vm::onItemClick,
            onDialogDismiss = vm::onDialogDismiss,
            onConfirmPurchase = vm::onConfirmPurchase,
        )
    }
}