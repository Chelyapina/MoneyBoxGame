package com.example.designsystem.components


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.theme.BlueBase
import com.example.designsystem.theme.MoneyBoxGameTheme
import com.example.designsystem.R

private val OwlHeadHeight = 66.dp
private val OwlWidth = 160.dp

@Composable
fun InformCard(
    title: String? = null,
    visible: Boolean = true,
    okText: String = stringResource(R.string.ok_text),
    okEnabled: Boolean = true,
    dismissText: String = stringResource(R.string.dismiss_text),
    buttonColor: Color = BlueBase,
    buttonContentColor: Color = Color.White,
    buttonShape: Shape = RoundedCornerShape(8.dp),
    onOkClick: () -> Unit,
    onDismissClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    val owlOffset by animateDpAsState(
        targetValue = if (visible) 0.dp else OwlHeadHeight,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "owl_offset",
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .width(OwlWidth)
                .height(OwlHeadHeight)
                .clipToBounds()
                .offset(y = owlOffset)
        ) {
            Image(
                painter = painterResource(id = R.drawable.owl_color),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = OwlHeadHeight),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (title != null || onBackClick != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (title != null) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 26.sp,
                                modifier = Modifier.weight(1f),
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                        onBackClick?.let {
                            IconButton(onClick = it) {
                                Icon(Icons.Default.Close, contentDescription = "Закрыть")
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }

                content?.invoke()

                Spacer(Modifier.height(16.dp))

                ButtonSection(
                    okText = okText,
                    okEnabled = okEnabled,
                    dismissText = dismissText,
                    buttonColor = buttonColor,
                    buttonContentColor = buttonContentColor,
                    buttonShape = buttonShape,
                    onOkClick = onOkClick,
                    onDismissClick = onDismissClick,
                )
            }
        }
    }
}

@Composable
fun ButtonSection(
    okText: String,
    okEnabled: Boolean,
    dismissText: String,
    buttonColor: Color,
    buttonContentColor: Color,
    buttonShape: Shape,
    onOkClick: () -> Unit,
    onDismissClick: (() -> Unit)?,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onDismissClick != null) {
            OutlinedButton(
                onClick = onDismissClick,
                shape = buttonShape,
                border = BorderStroke(1.5.dp, buttonColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = buttonColor,
                ),
            ) {
                Text(dismissText)
            }
            Spacer(Modifier.width(8.dp))
        }
        Button(
            onClick = onOkClick,
            enabled = okEnabled,
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = buttonContentColor,
            ),
        ) {
            Text(okText)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B1B1B)
@Preview(showBackground = true, backgroundColor = 0xFF1B1B1B)
@Composable
fun InformCardPreview() {
    MoneyBoxGameTheme {
        Box(Modifier.padding(16.dp)) {
            InformCard(
                title = "Сова на связи",
                okText = "Посмотреть",
                dismissText = "Позже",
                onOkClick = {},
                onDismissClick = {},
                onBackClick = {}
            ) {
                Text(
                    text = "У тебя новое уведомление. Открыть?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}