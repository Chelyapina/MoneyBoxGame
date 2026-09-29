package com.example.home.presentation.models

import androidx.annotation.DrawableRes
import com.example.designsystem.R

enum class OwlEyeColorUi(
    @DrawableRes val owlRes: Int
) {
    BLUE(R.drawable.anim_blue),
    YELLOW(R.drawable.anim_yellow),
    VIOLET(R.drawable.anim_violet),
    GREEN(R.drawable.anim_green)
}