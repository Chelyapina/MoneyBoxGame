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

private data class CartItem(
    val id: String,
    val emoji: String,
    val name: String,
    val price: Int,
    val obligatory: Boolean,
)

private val cartItems = listOf(
    CartItem("bread", "🍞", "Хлеб", 30, true),
    CartItem("milk", "🥛", "Молоко", 40, true),
    CartItem("apple", "🍎", "Яблоки", 15, true),
    CartItem("choco", "🍫", "Шоколадка", 25, false),
    CartItem("toy", "🧸", "Игрушка", 80, false),
)

private const val BUDGET = 100

@Composable
fun BudgetCartTask(onFinish: (TaskResult) -> Unit) {
    var cart by rememberSaveable { mutableStateOf(setOf<String>()) }
    val total = cartItems.filter { it.id in cart }.sumOf { it.price }
    val remaining = BUDGET - total

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Баланс
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Бюджет: $BUDGET", color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                text = if (remaining >= 0) "Осталось: $remaining" else "Перебор: ${-remaining}",
                color = if (remaining >= 0) Color(0xFF8BC34A) else Color(0xFFE53935),
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.height(4.dp))

        cartItems.forEach { item ->
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
                    if (item.obligatory) {
                        Text("нужное", color = Color(0xFF2E7D32), style = MaterialTheme.typography.labelSmall)
                    }
                }
                Text("${item.price}", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                onFinish(evaluateBudget(cart, total))
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("В магазин!")
        }
    }
}

private fun evaluateBudget(cart: Set<String>, total: Int): TaskResult {
    val obligatoryBought = cartItems
        .filter { it.obligatory }
        .count { it.id in cart }
    val allObligatory = cartItems.filter { it.obligatory }.size

    return when {
        total > BUDGET -> TaskResult(
            stars = 1,
            title = "Не хватает монеток",
            explanation = "Ты набрал на $total монеток, а в кошельке только $BUDGET. " +
                    "Прежде чем идти на кассу, посчитай сумму — так ты не окажешься в неловкой ситуации.",
        )
        obligatoryBought < allObligatory -> TaskResult(
            stars = 1,
            title = "Забыл про нужное",
            explanation = "Хлеб, молоко и яблоки — это обязательные покупки, без них не обойтись. " +
                    "Сначала кладём в корзину то, что нужно, а уже потом — то, что хочется.",
        )
        total == BUDGET -> TaskResult(
            stars = 3,
            title = "Идеально!",
            explanation = "Ты купил всё нужное и уложился ровно в бюджет. " +
                    "Планировать покупки заранее — самый верный способ не потратить лишнего.",
        )
        else -> TaskResult(
            stars = 2,
            title = "Хорошо!",
            explanation = "Ты купил нужное и оставил ${BUDGET - total} монеток про запас. " +
                    "Откладывать немного на будущее — отличная привычка!",
        )
    }
}