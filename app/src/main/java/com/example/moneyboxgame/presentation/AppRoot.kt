package com.example.moneyboxgame.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneyboxgame.navigation.AppNavHost

@Composable
fun AppRoot(vmFactory: ViewModelProvider.Factory, modifier: Modifier = Modifier) {
    val vm: SplashViewModel = viewModel(factory = vmFactory)
    val dest by vm.startDestination.collectAsStateWithLifecycle()

    Box(modifier.fillMaxSize()) {
        when (val d = dest) {
            null -> Unit
            else -> AppNavHost(startDestination = d, vmFactory = vmFactory)
        }
    }
}