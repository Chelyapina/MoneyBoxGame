package com.example.home.presentation.models

import androidx.annotation.DrawableRes
import com.example.designsystem.R

enum class OwlMoodUi(
    @DrawableRes val icons: List<Int>
) {
    BAD(listOf(R.drawable.red_exclamation_mark_3d, R.drawable.white_exclamation_mark_3d)),
    NEUTRAL(emptyList()),
    GOOD(listOf(R.drawable.red_heart_3d))
}