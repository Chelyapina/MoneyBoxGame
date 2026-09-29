package com.example.tasks.domain

import com.example.data.gameState.data.TaskReward

enum class TaskTheme(val title: String) {
    BUDGET("Планирование бюджета"),
    SAVINGS("Сбережения"),
    PAYMENTS("Платежи и покупки"),
}

data class TaskResult(
    val stars: Int,
    val title: String,
    val explanation: String,
    val reward: TaskReward = TaskReward.Standard,
)

data class FinancialTask(
    val id: String,
    val theme: TaskTheme,
    val emoji: String,
    val title: String,
    val scene: String,
)

object TaskCatalog {
    val tasks = listOf(
        // BUDGET
        FinancialTask(
            id = "budget_cart",
            theme = TaskTheme.BUDGET,
            emoji = "🛒",
            title = "Корзина в магазине",
            scene = "Сова проголодалась. У тебя 100 монеток. Собери корзину так, чтобы хватило на всё нужное.",
        ),
        FinancialTask(
            id = "budget_birthday",
            theme = TaskTheme.BUDGET,
            emoji = "🎂",
            title = "День рождения",
            scene = "У тебя 200 монеток на праздник. Выбери, что купить: подарок, угощение и шарики.",
        ),
        // SAVINGS
        FinancialTask(
            id = "savings_bike",
            theme = TaskTheme.SAVINGS,
            emoji = "🚲",
            title = "Копим на велосипед",
            scene = "Сова мечтает о велосипеде за 100 монеток. У неё есть 20. 5 дней по 20 монеток — реши, сколько откладывать.",
        ),
        FinancialTask(
            id = "savings_gift",
            theme = TaskTheme.SAVINGS,
            emoji = "🎁",
            title = "Подарок маме",
            scene = "Хочешь подарить маме цветы за 300 монеток. Сколько откладывать каждую неделю из 60 монеток дохода?",
        ),
        // PAYMENTS
        FinancialTask(
            id = "payments_change",
            theme = TaskTheme.PAYMENTS,
            emoji = "🍦",
            title = "Сдача в магазине",
            scene = "Ты купил мороженое за 47 монеток. У тебя банкнота 100 и монетка 50. Сколько дать кассиру?",
        ),
        FinancialTask(
            id = "payments_qr",
            theme = TaskTheme.PAYMENTS,
            emoji = "📱",
            title = "Оплата по QR-коду",
            scene = "Кассир просит оплатить по QR-коду. Какой код отсканируешь?",
        ),
    )
}