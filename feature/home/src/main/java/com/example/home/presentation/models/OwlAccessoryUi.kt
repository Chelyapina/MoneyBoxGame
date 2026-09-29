package com.example.home.presentation.models

import androidx.annotation.DrawableRes
import com.example.designsystem.R

enum class OwlAccessoryUi(
    @DrawableRes val resId: Int,
    val sizeRatio: Float,
    val offsetRatio: Float
) {
    HAT(
        resId = R.drawable.womans_hat_3d,
        sizeRatio = 0.35f,
        offsetRatio = -0.15f
    ),
    CAP(
        resId = R.drawable.billed_cap_3d,
        sizeRatio = 0.35f,
        offsetRatio = -0.08f
    ),
    GRADUATION_CAP(
        resId = R.drawable.graduation_cap_3d,
        sizeRatio = 0.50f,
        offsetRatio = -0.18f
    ),
    CROWN(
        resId = R.drawable.crown_3d,
        sizeRatio = 0.25f,
        offsetRatio = -0.08f
    )
}