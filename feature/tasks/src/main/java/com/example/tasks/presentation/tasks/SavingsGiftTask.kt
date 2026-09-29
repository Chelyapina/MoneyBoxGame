package com.example.tasks.presentation.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasks.domain.TaskResult
import kotlin.math.ceil
import kotlin.math.roundToInt

private const val GIFT_PRICE = 300
private const val WEEKLY_INCOME = 60
private const val WEEKS_AVAILABLE = 8

@Composable
fun SavingsGiftTask(onFinish: (TaskResult) -> Unit) {
    var weekly by rememberSaveable { mutableIntStateOf(30) }
    val weeksNeeded = if (weekly <= 0) Int.MAX_VALUE else ceil(GIFT_PRICE.toDouble() / weekly).toInt()
    val fits = weeksNeeded <= WEEKS_AVAILABLE

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "🌸 Цветы для мамы: $GIFT_PRICE монеток",
            color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold,
        )
        Text(
            "Каждую неделю ты получаешь $WEEKLY_INCOME монеток. У тебя есть $WEEKS_AVAILABLE недель.",
            color = Color.White.copy(alpha = 0.8f),
        )

        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Откладывать в неделю:", color = Color.White)
                Text("$weekly", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
            Slider(
                value = weekly.toFloat(),
                onValueChange = { weekly = it.roundToInt().coerceIn(0, WEEKLY_INCOME) },
                valueRange = 0f..WEEKLY_INCOME.toFloat(),
                steps = (WEEKLY_INCOME - 1).coerceAtLeast(0),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f),
                ),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Нужно недель:", color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                text = if (weekly == 0) "∞" else "$weeksNeeded",
                color = if (fits) Color(0xFF8BC34A) else Color(0xFFE53935),
                fontWeight = FontWeight.Bold,
            )
        }

        Button(
            onClick = { onFinish(evaluateGift(weekly, weeksNeeded)) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Начать копить")
        }
    }
}

private fun evaluateGift(weekly: Int, weeksNeeded: Int): TaskResult = when {
    weekly == 0 -> TaskResult(
        1, "Ничего не отложил",
        "Если не откладывать ничего, цель никогда не приблизится. " +
                "Начни хотя бы с 10 монеток в неделю!",
    )
    weeksNeeded <= 4 -> TaskResult(
        3, "Быстро копишь!",
        "Ты откладываешь $weekly монеток в неделю — это $weeksNeeded недель до подарка. " +
                "Совет: чем больше откладываешь сейчас, тем быстрее получаешь то, что хочешь.",
    )
    weeksNeeded <= WEEKS_AVAILABLE -> TaskResult(
        2, "Получится!",
        "Ты накопишь за $weeksNeeded недель из $WEEKS_AVAILABLE. Успеваешь! " +
                "Можно откладывать больше, чтобы закончить быстрее.",
    )
    else -> TaskResult(
        1, "Не успеешь",
        "При $weekly монетках в неделю нужно $weeksNeeded недель, а у тебя только $WEEKS_AVAILABLE. " +
                "Попробуй откладывать больше — так цель станет ближе.",
    )
}