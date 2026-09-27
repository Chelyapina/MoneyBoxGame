package com.example.moneyboxgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.designsystem.theme.MoneyBoxGameTheme
import com.example.moneyboxgame.presentation.AppRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = (application as MoneyBoxApp).component.viewModelFactory()

        setContent {
            MoneyBoxGameTheme {
                Scaffold(Modifier.fillMaxSize()) { inner ->
                    AppRoot(
                        vmFactory = factory,
                        modifier = Modifier.padding(inner),
                    )
                }
            }
        }
    }
}