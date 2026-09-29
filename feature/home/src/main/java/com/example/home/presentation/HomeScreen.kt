package com.example.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.gameState.data.GameState
import com.example.designsystem.R
import com.example.designsystem.background.OwlPatternLayer
import com.example.designsystem.components.InformCard
import com.example.designsystem.components.MoneyBoxBottomBar
import com.example.designsystem.components.MoneyBoxTopBar
import com.example.designsystem.resources.GoalCatalog
import com.example.designsystem.resources.ShopItem
import com.example.designsystem.resources.ShopItems
import com.example.designsystem.theme.GreenBgBottom
import com.example.designsystem.theme.GreenBgCenter
import com.example.designsystem.theme.GreenBgTop
import com.example.designsystem.util.modalBlock
import com.example.home.OwlWithAccessory
import com.example.home.R as LocalR

@Composable
fun HomeScreen(
    state: GameState,
    currentRoute: String?,
    onGoalPicked: (ShopItem) -> Unit,
    onTabClick: (String) -> Unit,
    onSavingsClick: () -> Unit,
    onSatietyClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val goal: ShopItem? = ShopItems.findById(state.goalId)

    var selectedGoalId by rememberSaveable { mutableStateOf<String?>(null) }
    var showConfirm by rememberSaveable { mutableStateOf(false) }

    val showSelection = state.goalId == null && !showConfirm
    var confirmGoalId by rememberSaveable { mutableStateOf<String?>(null) }
    val confirmGoal = confirmGoalId?.let(ShopItems::findById)
    val showConfirmDialog = showConfirm && confirmGoal != null

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GreenBgTop, GreenBgCenter)
                )
            )
    ) {
        OwlPatternLayer(
            modifier = Modifier.fillMaxSize(),
            color = GreenBgBottom,
            alpha = 0.3f,
            cellSize = 100.dp,
            owlSize = 48.dp,
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            MoneyBoxTopBar(
                coins = state.coins,
                balance = state.savings,
                eatLevel = state.satiety,
                onSavingsClick = onSavingsClick,
                onSatietyClick = onSatietyClick,
                onSettingsClick = onSettingsClick,
            )

            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.thought_balloon_3d),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-12).dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (goal != null) {
                        Image(
                            painter = painterResource(id = goal.iconRes),
                            contentDescription = stringResource(goal.nameRes),
                            modifier = Modifier.size(56.dp)
                        )
                        Text(
                            text = "${state.savings}/${goal.price}",
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    } else {
                        Text(
                            modifier = Modifier.padding(top = 14.dp),
                            text = stringResource(LocalR.string.dream_text),
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }

                }
            }
            OwlWithAccessory(
                eyeColor = state.owlEyeColor.toUi(),
                owlSize = state.owlSize.toUi(),
                accessory = state.owlAccessory.toUiOrNull(),
                mood = state.owlMood.toUi(),
            )
            state.petName?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }

            MoneyBoxBottomBar(
                currentRoute = currentRoute,
                onTabClick = onTabClick,
            )
        }

        if (state.goalId == null && confirmGoal == null) {
            GoalSelectionDialog(
                goals = GoalCatalog.forLevel(state.level),
                onGoalSelected = { picked ->
                    onGoalPicked(picked)
                    confirmGoalId = picked.id
                },
            )
        }

        if (confirmGoal != null) {
            GoalConfirmDialog(
                goal = confirmGoal,
                onSavingsClick = {
                    confirmGoalId = null
                    onSavingsClick()
                },
            )
        }
    }
}

@Composable
private fun GoalSelectionDialog(
    goals: List<ShopItem>,
    onGoalSelected: (ShopItem) -> Unit,
) {
    var selected by rememberSaveable {
        mutableStateOf(goals.firstOrNull()?.id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .modalBlock(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            InformCard(
                title = stringResource(LocalR.string.goal_select_title),
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = stringResource(LocalR.string.goal_select_text),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            goals.forEach { goal ->
                                GoalOptionCard(
                                    goal = goal,
                                    isSelected = goal.id == selected,
                                    onClick = { selected = goal.id },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                },
                okEnabled = selected != null,
                visible = true,
                onOkClick = {
                    goals.firstOrNull { it.id == selected }
                        ?.let(onGoalSelected)
                },
            )
        }
    }
}

@Composable
private fun GoalOptionCard(
    goal: ShopItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }
    val bg = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp),
            )
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = goal.iconRes),
            contentDescription = stringResource(goal.nameRes),
            modifier = Modifier.size(48.dp),
        )
        Text(
            text = stringResource(goal.nameRes),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = goal.price.toString(),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun GoalConfirmDialog(
    goal: ShopItem,
    onSavingsClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .modalBlock(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            InformCard(
                title = stringResource(LocalR.string.goal_confirm_title),
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Image(
                            painter = painterResource(id = goal.iconRes),
                            contentDescription = stringResource(goal.nameRes),
                            modifier = Modifier.size(88.dp),
                        )
                        Text(
                            text = stringResource(
                                LocalR.string.goal_confirm_text,
                                stringResource(goal.nameRes),
                                goal.price,
                            ),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                        )
                    }
                },
                okText = stringResource(LocalR.string.my_coins),
                onOkClick = onSavingsClick,
            )
        }
    }
}