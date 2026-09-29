package com.example.tasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.gameState.data.TaskReward
import com.example.designsystem.background.OwlPatternLayer
import com.example.designsystem.components.InformCard
import com.example.designsystem.theme.BlueBase
import com.example.designsystem.theme.BlueBgTop
import com.example.tasks.domain.FinancialTask
import com.example.tasks.domain.TaskCatalog
import com.example.tasks.domain.TaskResult
import com.example.tasks.presentation.tasks.BudgetBirthdayTask
import com.example.tasks.presentation.tasks.BudgetCartTask
import com.example.tasks.presentation.tasks.PaymentsChangeTask
import com.example.tasks.presentation.tasks.PaymentsQrTask
import com.example.tasks.presentation.tasks.SavingsBikeTask
import com.example.tasks.presentation.tasks.SavingsGiftTask

@Composable
fun TasksScreen(
    onBack: () -> Unit,
    onTaskCompleted: (TaskReward) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTaskId by rememberSaveable { mutableStateOf<String?>(null) }
    val selected = selectedTaskId?.let { id -> TaskCatalog.tasks.firstOrNull { it.id == id } }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BlueBgTop, BlueBase)))
    ) {
        OwlPatternLayer(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            alpha = 0.08f,
            cellSize = 96.dp,
            owlSize = 64.dp,
        )

        if (selected == null) {
            TaskList(
                onBack = onBack,
                onTaskClick = { selectedTaskId = it.id },
            )
        } else {
            TaskDetail(
                task = selected,
                onBack = { selectedTaskId = null },
                onTaskCompleted = onTaskCompleted,
            )
        }
    }
}

@Composable
private fun TaskList(
    onBack: () -> Unit,
    onTaskClick: (FinancialTask) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        TopBar(title = "Задания", onBack = onBack)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(TaskCatalog.tasks, key = { it.id }) { task ->
                TaskCard(task = task, onClick = { onTaskClick(task) })
            }
        }
    }
}

@Composable
private fun TaskCard(
    task: FinancialTask,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = task.emoji, fontSize = 42.sp)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = task.theme.title,
                color = BlueBase,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = task.title,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = task.scene,
                color = Color.Black.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
            )
        }
    }
}

@Composable
private fun TopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Назад",
                tint = Color.White,
            )
        }
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun TaskDetail(
    task: FinancialTask,
    onBack: () -> Unit,
    onTaskCompleted: (TaskReward) -> Unit,
) {
    var result by remember { mutableStateOf<TaskResult?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Топбар
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = task.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.width(12.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = task.scene,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                )
                Spacer(Modifier.height(16.dp))

                when (task.id) {
                    "budget_cart" -> BudgetCartTask(onFinish = { result = it })
                    "budget_birthday" -> BudgetBirthdayTask(onFinish = { result = it })
                    "savings_bike" -> SavingsBikeTask(onFinish = { result = it })
                    "savings_gift" -> SavingsGiftTask(onFinish = { result = it })
                    "payments_change" -> PaymentsChangeTask(onFinish = { result = it })
                    "payments_qr" -> PaymentsQrTask(onFinish = { result = it })
                }
            }
        }

        // Диалог — поверх всего Box, а не внутри Column
        result?.let { r ->
            TaskResultDialog(
                result = r,
                onDismiss = {
                    result = null
                    onTaskCompleted(r.reward)
                },
            )
        }
    }
}

@Composable
fun TaskResultDialog(
    result: TaskResult,
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            InformCard(
                title = result.title,
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            repeat(3) { i ->
                                Text(
                                    text = if (i < result.stars) "⭐" else "☆",
                                    fontSize = 32.sp,
                                )
                            }
                        }
                        Text(
                            text = result.explanation,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        // Плашка с наградой
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF8BC34A).copy(alpha = 0.15f))
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                "🪙 +${result.reward.foodCoins + result.reward.funCoins}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                            )
                            Text(
                                "🍽 −${result.reward.satietyCost} сытости",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE65100),
                            )
                        }
                    }
                },
                okText = "Понятно",
                visible = true,
                onOkClick = onDismiss,
            )
        }
    }
}