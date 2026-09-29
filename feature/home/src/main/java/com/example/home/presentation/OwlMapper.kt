package com.example.home.presentation

import androidx.compose.ui.unit.Dp
import com.example.data.gameState.data.OwlAccessory
import com.example.data.gameState.data.OwlEyeColor
import com.example.data.gameState.data.OwlMood
import com.example.data.gameState.data.OwlSize
import com.example.home.presentation.models.OwlAccessoryUi
import com.example.home.presentation.models.OwlEyeColorUi
import com.example.home.presentation.models.OwlMoodUi
import com.example.home.presentation.models.OwlSizesUi

fun OwlMood.toUi(): OwlMoodUi = when (this) {
    OwlMood.BAD -> OwlMoodUi.BAD
    OwlMood.NEUTRAL -> OwlMoodUi.NEUTRAL
    OwlMood.GOOD -> OwlMoodUi.GOOD
}

fun OwlAccessory.toUi(): OwlAccessoryUi = when (this) {
    OwlAccessory.HAT -> OwlAccessoryUi.HAT
    OwlAccessory.CAP -> OwlAccessoryUi.CAP
    OwlAccessory.GRADUATION_CAP -> OwlAccessoryUi.GRADUATION_CAP
    OwlAccessory.CROWN -> OwlAccessoryUi.CROWN
}

fun OwlAccessory?.toUiOrNull(): OwlAccessoryUi? = this?.toUi()

fun OwlEyeColor.toUi(): OwlEyeColorUi = when (this) {
    OwlEyeColor.BLUE -> OwlEyeColorUi.BLUE
    OwlEyeColor.YELLOW -> OwlEyeColorUi.YELLOW
    OwlEyeColor.VIOLET -> OwlEyeColorUi.VIOLET
    OwlEyeColor.GREEN -> OwlEyeColorUi.GREEN
}

fun OwlSize.toUi(): Dp = when (this) {
    OwlSize.SMALL -> OwlSizesUi.Small
    OwlSize.MEDIUM -> OwlSizesUi.Medium
    OwlSize.LARGE -> OwlSizesUi.Large
}