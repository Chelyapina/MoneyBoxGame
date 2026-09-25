package com.example.onboarding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.designsystem.theme.MoneyBoxGameTheme

@Composable
fun OnboardingScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Hello world!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    MoneyBoxGameTheme {
        OnboardingScreen()
    }
}