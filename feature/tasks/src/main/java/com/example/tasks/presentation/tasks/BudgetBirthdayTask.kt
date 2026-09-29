package com.example.tasks.presentation.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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

private data class PartyItem(
    val id: String, val emoji: String, val name: String, val price: Int,
    val priority: Int,
)

private val partyItems = listOf(
    PartyItem("gift",     "🎁", "Подарок другу",   80, 1),
    PartyItem("cake",     "🎂", "Торт",            70, 1),
    PartyItem("juice",    "🧃", "Сок",             30, 2),
    PartyItem("balloons", "🎈", "Шарики",          20, 3),
    PartyItem("candy",    "🍬", "Конфеты",         40, 3),
)

private const val PARTY_BUDGET = 200

@Composable
fun BudgetBirthdayTask(onFinish: (TaskResult) -> Unit) {
    var cart by rememberSaveable { mutableStateOf(setOf<String>()) }
    val total = partyItems.filter { it.id in cart }.sumOf { it.price }
    val remaining = PARTY_BUDGET - total

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Бюджет: $PARTY_BUDGET", color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                text = if (remaining >= 0) "Осталось: $remaining" else "Перебор: ${-remaining}",
                color = if (remaining >= 0) Color(0xFF8BC34A) else Color(0xFFE53935),
                fontWeight = FontWeight.Bold,
            )
        }

        partyItems.forEach { item ->
            val inCart = item.id in cart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (inCart) Color(0xFFFFF3C4) else Color.White.copy(alpha = 0.9f)
                    )
                    .clickable {
                        cart = if (inCart) cart - item.id else cart + item.id
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(item.emoji, fontSize = 32.sp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(item.name, fontWeight = FontWeight.Bold)
                    Text(
                        when (item.priority) {
                            1 -> "самое важное"
                            2 -> "нужно"
                            else -> "приятный бонус"
                        },
                        color = when (item.priority) {
                            1 -> Color(0xFF2E7D32)
                            2 -> Color(0xFF1976D2)
                            else -> Color(0xFF9E9E9E)
                        },
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
                Text("${item.price}", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { onFinish(evaluateParty(cart, total)) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Готово!")
        }
    }
}

private fun evaluateParty(cart: Set<String>, total: Int): TaskResult {
    val priority1 = partyItems.filter { it.priority == 1 }.map { it.id }
    val hasAllP1 = priority1.all { it in cart }

    return when {
        total > PARTY_BUDGET -> TaskResult(
            1, "Слишком много",
            "Ты выбрал на $total монеток, а бюджет $PARTY_BUDGET. " +
                    "Сначала посчитай сумму, потом иди покупать.",
        )
        !hasAllP1 -> TaskResult(
            1, "Забыл главное",
            "Подарок и торт — самое важное на дне рождения. " +
                    "Без них праздник не получится. Начинать всегда с главного!",
        )
        total >= PARTY_BUDGET - 30 -> TaskResult(
            3, "Отличный план!",
            "Ты купил всё главное и почти всё приятное, уложился в бюджет. " +
                    "Расставлять приоритеты — важное умение!",
        )
        else -> TaskResult(
            2, "Хорошо",
            "Ты купил главное и оставил ${PARTY_BUDGET - total} монеток. " +
                    "Так тоже можно — запас никогда не помешает.",
        )
    }
}