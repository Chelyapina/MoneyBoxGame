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
import kotlin.math.roundToInt

private const val PRICE = 47
private const val MAX_PAY = 100

@Composable
fun PaymentsChangeTask(onFinish: (TaskResult) -> Unit) {
    var pay by rememberSaveable { mutableIntStateOf(50) }
    val change = pay - PRICE

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("🍦 Мороженое: $PRICE монеток", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Text(
            text = "У тебя есть банкнота 100 и монетка 50.",
            color = Color.White.copy(alpha = 0.8f),
        )

        // Слайдер: сколько дать кассиру
        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Даёшь кассиру:", color = Color.White)
                Text("$pay", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
            Slider(
                value = pay.toFloat(),
                onValueChange = { pay = it.roundToInt() },
                valueRange = PRICE.toFloat()..MAX_PAY.toFloat(),
                steps = (MAX_PAY - PRICE - 1).coerceAtLeast(0),
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
            Text("Сдача:", color = Color.White, fontWeight = FontWeight.Bold)
            Text("$change", color = Color(0xFF8BC34A), fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = { onFinish(evaluatePayment(pay)) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Оплатить")
        }
    }
}

private fun evaluatePayment(pay: Int): TaskResult {
    val change = pay - PRICE
    return when {
        pay == 50 -> TaskResult(
            stars = 3,
            title = "Отлично!",
            explanation = "Ты дал 50 — сдача ровно 3 монетки. Платить близко к цене удобно: " +
                    "и тебе проще проверить сдачу, и у кассира не закончатся мелкие.",
        )
        pay == 100 -> TaskResult(
            stars = 2,
            title = "Тоже верно",
            explanation = "Сдача 53 монетки. Так тоже можно, но чем крупнее сумма, тем легче " +
                    "ошибиться. Всегда пересчитывай сдачу и бери чек.",
        )
        pay in 48..99 -> TaskResult(
            stars = 2,
            title = "Норм, но не идеально",
            explanation = "Сдача $change. Ничего страшного, но обычно платят так, чтобы сдача " +
                    "была удобной — например, ровно 50. Так проще проверять.",
        )
        else -> TaskResult(
            stars = 1,
            title = "Не хватит",
            explanation = "Ты дал $pay, а мороженое стоит $PRICE. Так не получится — " +
                    "всегда проверяй, хватает ли денег до того, как идёшь на кассу.",
        )
    }
}