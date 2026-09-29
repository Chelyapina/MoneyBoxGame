package com.example.designsystem.util

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.modalBlock(
    onBackdropClick: (() -> Unit)? = null,
): Modifier = composed {
    pointerInput(onBackdropClick) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                val anyPressed = event.changes.any { it.pressed }
                if (anyPressed) {
                    event.changes.forEach { it.consume() }
                    if (event.changes.all { !it.pressed }) {
                        onBackdropClick?.invoke()
                    }
                }
            }
        }
    }
}