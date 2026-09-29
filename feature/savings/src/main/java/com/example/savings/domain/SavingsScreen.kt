package com.example.savings.domain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.gameState.data.GameState
import com.example.designsystem.background.OwlPatternLayer
import com.example.designsystem.components.InformCard
import com.example.designsystem.theme.PurpleBgBottom
import com.example.designsystem.theme.PurpleBgCenter
import com.example.designsystem.theme.PurpleBgTop
import com.example.savings.R
import kotlin.math.roundToInt

@Composable
fun SavingsScreen(
    state: GameState,
    onBack: () -> Unit,
    onPlanConfirmed: (food: Int, funAmount: Int, savings: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val total = state.balanceFood + state.balanceFun + state.savings

    var food by rememberSaveable(total) { mutableIntStateOf(state.balanceFood) }
    var funAmount by rememberSaveable(total) { mutableIntStateOf(state.balanceFun) }
    var savings by rememberSaveable(total) { mutableIntStateOf(state.savings) }

    val remaining = total - (food + funAmount + savings)
    val allDistributed = remaining == 0

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleBgTop, PurpleBgCenter)))
    ) {
        OwlPatternLayer(
            modifier = Modifier.fillMaxSize(),
            color = PurpleBgBottom,
            alpha = 0.3f,
            cellSize = 100.dp,
            owlSize = 48.dp,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(8.dp))

            InformCard(
                title = stringResource(R.string.savings_title),
                content = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.savings_explanation),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                        )

                        Spacer(Modifier.height(16.dp))

                        RemainingCounter(
                            remaining = remaining,
                            total = total,
                        )

                        Spacer(Modifier.height(16.dp))

                        PlanSlider(
                            label = stringResource(R.string.plan_food),
                            value = food,
                            total = total,
                            maxAllowed = food + remaining,
                            onValueChange = { food = it },
                        )
                        PlanSlider(
                            label = stringResource(R.string.plan_fun),
                            value = funAmount,
                            total = total,
                            maxAllowed = funAmount + remaining,
                            onValueChange = { funAmount = it },
                        )
                        PlanSlider(
                            label = stringResource(R.string.plan_savings),
                            value = savings,
                            total = total,
                            maxAllowed = savings + remaining,
                            onValueChange = { savings = it },
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = stringResource(R.string.plan_total_fmt, total),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                visible = true,
                okEnabled = allDistributed,
                onOkClick = { onPlanConfirmed(food, funAmount, savings) },
                onBackClick = onBack,
            )
        }
    }
}

@Composable
private fun RemainingCounter(
    remaining: Int,
    total: Int,
) {
    val color = when {
        remaining == 0 -> Color(0xFF4CAF50)
        remaining > 0 -> Color(0xFFFF9800)
        else -> Color(0xFFE53935)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (remaining == 0) {
                stringResource(R.string.plan_all_distributed)
            } else {
                stringResource(R.string.plan_remaining_fmt, remaining)
            },
            textAlign = TextAlign.Center,
            color = color,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}
@Composable
private fun PlanSlider(
    label: String,
    value: Int,
    total: Int,
    maxAllowed: Int,
    onValueChange: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = label, fontWeight = FontWeight.Medium)
            Text(text = value.toString(), fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = {
                onValueChange(it.roundToInt().coerceIn(0, maxAllowed))
            },
            valueRange = 0f..total.toFloat().coerceAtLeast(1f),
            colors = SliderDefaults.colors(
                thumbColor = PurpleBgTop,
                activeTrackColor = PurpleBgTop,
                inactiveTrackColor = PurpleBgTop.copy(alpha = 0.25f),
            ),
        )
    }
}