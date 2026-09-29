package com.example.tasks.presentation.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasks.domain.TaskResult

private const val DAYS = 5
private const val DAILY_INCOME = 20
private const val START = 20
private const val GOAL = 100

@Composable
fun SavingsBikeTask(onFinish: (TaskResult) -> Unit) {
    var day by rememberSaveable { mutableIntStateOf(0) }
    var saved by rememberSaveable { mutableIntStateOf(START) }

    if (day >= DAYS) {
        LaunchedEffect(Unit) {
            onFinish(evaluateSavings(saved))
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "День ${day + 1} из $DAYS",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )

        // Прогресс
        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("🐷 Накоплено: $saved", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Цель: $GOAL", color = Color.White.copy(alpha = 0.7f))
            }
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { (saved.toFloat() / GOAL).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = Color(0xFF8BC34A),
                trackColor = Color.White.copy(alpha = 0.2f),
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Сова получила $DAILY_INCOME монеток. Что сделать?",
            color = Color.White,
            fontSize = 16.sp,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = {
                    day += 1
                    // потратил — ничего не отложил
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800),
                ),
            ) {
                Text("🍬 Потратить")
            }
            Button(
                onClick = {
                    saved += DAILY_INCOME
                    day += 1
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                ),
            ) {
                Text("🐷 Отложить")
            }
        }
    }
}

private fun evaluateSavings(saved: Int): TaskResult = when {
    saved >= GOAL -> TaskResult(
        stars = 3,
        title = "Велосипед твой!",
        explanation = "Ты накопил $saved монеток — этого хватает на велосипед. " +
                "Копить каждый день понемногу — самый надёжный путь к большой цели.",
    )
    saved >= GOAL - 20 -> TaskResult(
        stars = 2,
        title = "Почти получилось",
        explanation = "У тебя $saved из $GOAL. Ещё пара дней — и цель достигнута. " +
                "Даже одно «отложить» вместо «потратить» сильно меняет результат.",
    )
    else -> TaskResult(
        stars = 1,
        title = "Не хватило",
        explanation = "Накопил только $saved из $GOAL. Если бы ты откладывал каждый день, " +
                "мечта была бы ближе. Маленькие регулярные сбережения побеждают большие редкие.",
    )
}