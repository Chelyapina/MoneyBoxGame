package com.example.designsystem.background

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

@Composable
fun OwlPatternLayer(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    alpha: Float = 0.22f,
    cellSize: Dp = 96.dp,
    owlSize: Dp = 80.dp,
    owlRes: Int = com.example.designsystem.R.drawable.owl_high_contrast,
) {
    val owl = painterResource(owlRes)

    Canvas(modifier = modifier) {
        val step = cellSize.toPx()
        val s = owlSize.toPx()

        val cols = ceil(size.width / step).toInt() + 1
        val rows = ceil(size.height / step).toInt() + 1

        for (row in 0 until rows) {
            val rowOffset = if (row % 2 == 0) 0f else step / 2f

            for (col in 0 until cols) {
                val cx = col * step + rowOffset + step / 2f
                val cy = row * step + step / 2f

                withTransform({
                    translate(left = cx - s / 2f, top = cy - s / 2f)
                }) {
                    with(owl) {
                        draw(
                            size = Size(s, s),
                            alpha = alpha,
                            colorFilter = ColorFilter.tint(color),
                        )
                    }
                }
            }
        }
    }
}