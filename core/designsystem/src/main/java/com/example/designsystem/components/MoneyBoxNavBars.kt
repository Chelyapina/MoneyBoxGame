package com.example.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AccentColor
import com.example.designsystem.theme.BorderWhiteFade
import com.example.designsystem.theme.BottomBarAccentColor
import com.example.designsystem.theme.BottomBarBorderWhiteFade
import com.example.designsystem.theme.BottomBarGradientBottom
import com.example.designsystem.theme.BottomBarGradientTop
import com.example.designsystem.theme.BottomBarShadowColor
import com.example.designsystem.theme.GreenBgCenter
import com.example.designsystem.theme.GreenBgTop
import com.example.designsystem.theme.MoneyBoxGameTheme
import com.example.designsystem.theme.SatietyHighColor
import com.example.designsystem.theme.SatietyLowColor
import com.example.designsystem.theme.SatietyMediumColor
import com.example.designsystem.theme.SatietyTrackColor
import com.example.designsystem.theme.ShadowColor
import com.example.designsystem.theme.TabUnselectedTextColor
import com.example.designsystem.theme.TopBarGradientBottom
import com.example.designsystem.theme.TopBarGradientTop

@Composable
fun MoneyBoxTopBar(
    modifier: Modifier = Modifier,
    coins: Int = 0,
    balance: Int = 0,
    eatLevel: Int = 0,
    onSavingsClick: () -> Unit = {},
    onSatietyClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    accent: Color = AccentColor,
) {
    val shape = RoundedCornerShape(50)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 8.dp)
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = ShadowColor,
                spotColor = ShadowColor,
            )
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        TopBarGradientTop,
                        TopBarGradientBottom,
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(Color.White, BorderWhiteFade)
                ),
                shape = shape,
            )
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SavingsChip(
            coins = coins,
            balance = balance,
            accent = accent,
            onClick = onSavingsClick,
        )

        Spacer(Modifier.weight(1f))

        SatietyChip(
            satiety = eatLevel,
            onClick = onSatietyClick,
        )

        Spacer(Modifier.weight(1f))

        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings),
                modifier = Modifier.size(26.dp),
                tint = accent,
            )
        }
    }
}

@Composable
private fun SavingsChip(
    coins: Int,
    balance: Int,
    accent: Color = AccentColor,
    onClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(50.dp)
    Column(
        modifier = Modifier
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .background(Color.White)
            .border(1.dp, Color.White, shape)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.savings),
            textAlign = TextAlign.Center,
            color = accent,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.coin_color),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = coins.toString(),
                color = accent,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.width(10.dp))
            Image(
                painter = painterResource(R.drawable.money_bag_color),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = balance.toString(),
                color = accent,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun SatietyChip(
    satiety: Int,
    onClick: () -> Unit = {},
) {
    val clamped = satiety.coerceIn(0, 10)
    val (levelColor, zone) = when (clamped) {
        in 0..3 -> SatietyLowColor to 0
        in 4..6 -> SatietyMediumColor to 1
        else -> SatietyHighColor to 2
    }
    val filled = zone + 1
    val trackColor = SatietyTrackColor
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .background(Color.White)
            .border(1.dp, Color.White, shape)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.satiety),
                style = MaterialTheme.typography.labelLarge,
                color = levelColor,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "$clamped/10",
                style = MaterialTheme.typography.labelLarge,
                color = levelColor,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (i in 0 until 3) {
                val isFilled = i < filled
                val color by animateColorAsState(
                    targetValue = if (isFilled) levelColor else trackColor,
                    animationSpec = tween(220),
                    label = "segColor$i",
                )
                Box(
                    modifier = Modifier
                        .size(width = 22.dp, height = 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color),
                )
            }
        }
    }
}

private data class Tab(
    val route: String,
    @DrawableRes val icon: Int,
    val label: String,
)

@Composable
fun MoneyBoxBottomBar(
    currentRoute: String?,
    onTabClick: (String) -> Unit,
    accent: Color = BottomBarAccentColor,
) {
    val tabs = listOf(
        Tab("shop", R.drawable.shopping_cart_color, "Магазин"),
        Tab("home", R.drawable.house_color, "Главная"),
        Tab("tasks", R.drawable.memo_color, "Задания"),
    )

    val shape = RoundedCornerShape(50)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = shape,
                    ambientColor = BottomBarShadowColor,
                    spotColor = BottomBarShadowColor,
                )
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            BottomBarGradientTop,
                            BottomBarGradientBottom,
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(Color.White, BottomBarBorderWhiteFade)
                    ),
                    shape = shape,
                )
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEach { tab ->
                BottomTab(
                    tab = tab,
                    selected = currentRoute == tab.route,
                    accent = accent,
                    onClick = { onTabClick(tab.route) },
                )
            }
        }
    }
}

@Composable
private fun BottomTab(
    tab: Tab,
    selected: Boolean,
    accent: Color,
    onClick: () -> Unit,
) {
    val iconSize by animateDpAsState(
        targetValue = if (selected) 52.dp else 48.dp,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "iconSize",
    )
    val pillColor by animateColorAsState(
        targetValue = if (selected) accent.copy(alpha = 0.12f) else Color.Transparent,
        label = "pillColor",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) accent else TabUnselectedTextColor,
        label = "textColor",
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .background(pillColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(tab.icon),
            contentDescription = tab.label,
            modifier = Modifier.size(iconSize),
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = tab.label,
            color = textColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoneyNavBarsPreview() {

    MoneyBoxGameTheme {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(GreenBgTop, GreenBgCenter)
                    )
                )
        ) {
            MoneyBoxTopBar()
            MoneyBoxBottomBar(currentRoute = "home", onTabClick = {})
        }
    }
}