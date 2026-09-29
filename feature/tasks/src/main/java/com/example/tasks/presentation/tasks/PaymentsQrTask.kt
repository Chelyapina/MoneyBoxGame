package com.example.tasks.presentation.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasks.domain.TaskResult

private data class QrChoice(
    val id: String, val emoji: String, val title: String, val subtitle: String,
)

private val qrChoices = listOf(
    QrChoice("official", "🏪", "QR-код на кассе магазина",
        "Такой же, как у продавца на экране"),
    QrChoice("stranger", "📩", "QR-код из сообщения «Мама, срочно переведи!»",
        "Пришёл от незнакомого номера"),
    QrChoice("ad", "📢", "QR-код из рекламы в интернете",
        "«Пройди по нему и получи 1000 монет»"),
)

@Composable
fun PaymentsQrTask(onFinish: (TaskResult) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            "Какой QR-код отсканируешь?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )

        qrChoices.forEach { choice ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.9f))
                    .clickable { onFinish(evaluateQr(choice.id)) }
                    .padding(16.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(choice.emoji, fontSize = 32.sp)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        choice.title,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    choice.subtitle,
                    color = Color.Black.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

private fun evaluateQr(choice: String): TaskResult = when (choice) {
    "official" -> TaskResult(
        3, "Молодец!",
        "Оплата по QR-коду на кассе магазина — это нормально и безопасно. " +
                "Всегда проверяй, что сумма на экране совпадает с ценой.",
    )
    "stranger" -> TaskResult(
        1, "Опасно!",
        "Настоящая мама не пишет с незнакомого номера «срочно переведи». " +
                "Это мошенники. Позвони маме и проверь — никогда не переводи по чужому QR-коду.",
    )
    "ad" -> TaskResult(
        1, "Это ловушка",
        "«Получи 1000 монет по ссылке» — так действуют мошенники. " +
                "Бесплатные деньги в интернете бывают только в сказках. Не сканируй такие коды.",
    )
    else -> TaskResult(1, "Ошибка", "Попробуй ещё раз.")
}