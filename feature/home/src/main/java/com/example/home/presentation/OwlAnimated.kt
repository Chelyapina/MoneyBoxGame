package com.example.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.home.presentation.models.OwlAccessoryUi
import com.example.home.presentation.models.OwlEyeColorUi
import com.example.home.presentation.models.OwlMoodUi

@Composable
fun OwlWithAccessory(
    modifier: Modifier = Modifier,
    eyeColor: OwlEyeColorUi,
    owlSize: Dp,
    accessory: OwlAccessoryUi?,
    mood: OwlMoodUi = OwlMoodUi.NEUTRAL,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        if (mood != OwlMoodUi.NEUTRAL) {
            MoodIconsSide(
                mood = mood,
                iconSize = owlSize * 0.18f,
                modifier = Modifier
                    .width(owlSize * 1.2f)
                    .align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier.size(owlSize),
            contentAlignment = Alignment.TopCenter
        ) {
            AsyncImage(
                model = eyeColor.owlRes,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            if (accessory != null) {
                Image(
                    painter = painterResource(id = accessory.resId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(owlSize * accessory.sizeRatio)
                        .offset(y = owlSize * accessory.offsetRatio)
                )
            }
        }

        Branch(
            modifier = Modifier
                .fillMaxWidth()
                .height(owlSize * 0.12f)
                .align(Alignment.BottomCenter)
                .offset(y = -(owlSize * 0.01f))
        )
    }
}

@Composable
fun Branch(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF8B5A2B),
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val centerY = h / 2f
        val mainStroke = h * 0.28f
        val twigStroke = h * 0.16f

        drawLine(
            color = color,
            start = Offset(0f, centerY),
            end = Offset(w, centerY),
            strokeWidth = mainStroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(w * 0.25f, centerY),
            end = Offset(w * 0.15f, h * 0.05f),
            strokeWidth = twigStroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(w * 0.75f, centerY),
            end = Offset(w * 0.85f, h * 0.05f),
            strokeWidth = twigStroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(w * 0.55f, centerY),
            end = Offset(w * 0.6f, h * 0.95f),
            strokeWidth = twigStroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun MoodIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    size: Dp,
    tiltDirection: Int,
    phaseOffsetMs: Int = 0,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mood")

    val baseTilt = tiltDirection * 12f
    val rotation by infiniteTransition.animateFloat(
        initialValue = baseTilt - 8f,
        targetValue = baseTilt + 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                delayMillis = phaseOffsetMs,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                delayMillis = phaseOffsetMs,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Image(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        modifier = modifier
            .size(size)
            .graphicsLayer {
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
            }
    )
}

@Composable
fun MoodIconsSide(
    mood: OwlMoodUi,
    iconSize: Dp,
    modifier: Modifier = Modifier,
    iconsPerSide: Int = 1
) {
    val icons = mood.icons
    if (icons.isEmpty()) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SideColumn(
            icons = icons,
            iconSize = iconSize,
            tiltDirection = -1,
            phaseBase = 0,
            iconsPerSide = iconsPerSide
        )

        SideColumn(
            icons = icons,
            iconSize = iconSize,
            tiltDirection = 1,
            phaseBase = 65,
            iconsPerSide = iconsPerSide
        )
    }
}

@Composable
private fun SideColumn(
    icons: List<Int>,
    iconSize: Dp,
    tiltDirection: Int,
    phaseBase: Int,
    iconsPerSide: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(iconSize * 0.4f)
    ) {
        repeat(iconsPerSide) { i ->
            key(i) {
                MoodIcon(
                    iconRes = icons[i % icons.size],
                    size = iconSize,
                    tiltDirection = tiltDirection,
                    phaseOffsetMs = phaseBase + i * 150,
                    modifier = Modifier.offset(
                        y = if (i % 2 == 0) (-iconSize * 0.3f) else (iconSize * 0.3f)
                    )
                )
            }
        }
    }
}