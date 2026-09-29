package com.example.onboarding.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.data.gameState.data.OwlAccessory
import com.example.data.gameState.data.OwlEyeColor
import com.example.onboarding.R
import com.example.designsystem.R as DesignSystemR

private val EYE_COLORS = listOf(
    OwlEyeColor.YELLOW to Color(0xFFF8D41F),
    OwlEyeColor.BLUE to Color(0xFF119FF6),
    OwlEyeColor.VIOLET to Color(0xFF9E4AF2),
    OwlEyeColor.GREEN to Color(0xFF18CE07),
)

private val ACCESSORIES = listOf(
    null to DesignSystemR.drawable.prohibited_3d,
    OwlAccessory.HAT to DesignSystemR.drawable.womans_hat_3d,
    OwlAccessory.CAP to DesignSystemR.drawable.billed_cap_3d,
    OwlAccessory.GRADUATION_CAP to DesignSystemR.drawable.graduation_cap_3d,
    OwlAccessory.CROWN to DesignSystemR.drawable.crown_3d,
)

@Composable
fun PetViewDialogContent(
    eyeColor: OwlEyeColor,
    accessory: OwlAccessory?,
    onEyeColorChange: (OwlEyeColor) -> Unit,
    onAccessoryChange: (OwlAccessory?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            stringResource(R.string.pet_view_eye_color_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            EYE_COLORS.forEach { (color, uiColor) ->
                SelectableCircle(
                    color = uiColor,
                    selected = color == eyeColor,
                    onClick = { onEyeColorChange(color) },
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            stringResource(R.string.pet_view_accessory_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ACCESSORIES.forEach { (acc, res) ->
                SelectableIcon(
                    res = res,
                    selected = acc == accessory,
                    onClick = { onAccessoryChange(acc) },
                )
            }
        }
    }
}

@Composable
private fun SelectableCircle(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (selected) 4.dp else 0.dp,
                color = if (selected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
    )
}

@Composable
private fun SelectableIcon(
    @DrawableRes res: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .border(
                width = if (selected) 3.dp else 0.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(res),
            contentDescription = null,
            alignment = Alignment.Center,
            modifier = Modifier.size(32.dp),
        )
    }
}