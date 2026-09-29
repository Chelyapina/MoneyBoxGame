package com.example.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.data.gameState.data.GameState
import com.example.data.gameState.data.PurchaseOutcome
import com.example.designsystem.background.OwlPatternLayer
import com.example.designsystem.components.InformCard
import com.example.designsystem.resources.ItemCategory
import com.example.designsystem.resources.ShopItem
import com.example.designsystem.resources.ShopItems
import com.example.designsystem.theme.OrangeBgBottom
import com.example.designsystem.theme.OrangeBgCenter
import com.example.designsystem.theme.OrangeBgTop
import com.example.designsystem.util.modalBlock

@Composable
fun ShopScreen(
    state: GameState,
    dialogState: ShopDialogState?,
    onBack: () -> Unit,
    onItemClick: (ShopItem) -> Unit,
    onDialogDismiss: () -> Unit,
    onConfirmPurchase: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(OrangeBgTop, OrangeBgCenter),
                )
            )
    ) {
        OwlPatternLayer(
            modifier = Modifier.fillMaxSize(),
            color = OrangeBgBottom,
            alpha = 0.25f,
            cellSize = 100.dp,
            owlSize = 48.dp,
        )

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
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White,
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.coins_fmt, state.coins),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.width(12.dp))
            }

            val mandatory = remember { ShopItems.byCategory(ItemCategory.MANDATORY) }
            val optional = remember { ShopItems.byCategory(ItemCategory.OPTIONAL) }
            val goals = remember {
                ShopItems.byCategory(ItemCategory.GOAL).sortedBy { it.price }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item { SectionHeader(stringResource(R.string.shop_section_food)) }
                items(mandatory, key = { it.id }) { item ->
                    ShopItemCard(
                        item = item,
                        coins = state.coins,
                        onClick = { onItemClick(item) },
                    )
                }

                item { SectionHeader(stringResource(R.string.shop_section_fun)) }
                items(optional, key = { it.id }) { item ->
                    ShopItemCard(
                        item = item,
                        coins = state.coins,
                        onClick = { onItemClick(item) },
                    )
                }

                item { SectionHeader(stringResource(R.string.shop_section_goals)) }
                items(goals, key = { it.id }) { item ->
                    ShopItemCard(
                        item = item,
                        coins = state.coins,
                        isCurrentGoal = item.id == state.goalId,
                        onClick = { onItemClick(item) },
                    )
                }
            }
        }

        dialogState?.let { dialog ->
            ShopPurchaseDialog(
                state = dialog,
                onDismiss = onDialogDismiss,
                onConfirm = onConfirmPurchase,
            )
        }
    }
}

@Composable
private fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp),
        color = Color.White,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun ShopItemCard(
    item: ShopItem,
    coins: Int,
    isCurrentGoal: Boolean = false,
    onClick: () -> Unit,
) {
    val canAfford = coins >= item.price
    val isGoal = item.category == ItemCategory.GOAL

    val bg = when {
        isCurrentGoal -> Color(0xFFFFF3C4)
        isGoal -> Color.White.copy(alpha = 0.85f)
        canAfford -> Color.White.copy(alpha = 0.95f)
        else -> Color.White.copy(alpha = 0.6f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(item.iconRes),
            contentDescription = stringResource(item.nameRes),
            modifier = Modifier.size(56.dp),
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(item.nameRes),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            val subtitle = when {
                isGoal && isCurrentGoal -> stringResource(R.string.shop_goal_current)
                isGoal -> stringResource(R.string.shop_goal_future)
                item.category == ItemCategory.MANDATORY ->
                    stringResource(R.string.shop_effect_satiety_fmt, item.effect ?: 0)
                item.category == ItemCategory.OPTIONAL ->
                    stringResource(R.string.shop_effect_fun_fmt, item.effect ?: 0)
                else -> ""
            }
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black.copy(alpha = 0.7f),
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = item.price.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = when {
                isCurrentGoal -> OrangeBgBottom
                isGoal -> Color.Black.copy(alpha = 0.5f)
                canAfford -> OrangeBgBottom
                else -> Color.Gray
            },
        )
    }
}

@Composable
private fun ShopPurchaseDialog(
    state: ShopDialogState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val item = state.item
    val outcome = state.outcome
    val isSuccess = outcome is PurchaseOutcome.Success
    val isGoal = item.category == ItemCategory.GOAL

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)).modalBlock(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            InformCard(
                title = stringResource(item.nameRes),
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Image(
                            painter = painterResource(item.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(96.dp),
                        )
                        if (!isGoal) {
                            Text(
                                text = stringResource(R.string.shop_price_fmt, item.price),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Text(
                            text = dialogMessage(state),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                },
                okText = if (isSuccess) {
                    stringResource(R.string.shop_buy)
                } else {
                    stringResource(R.string.ok)
                },
                okEnabled = if (isGoal) true else isSuccess,
                visible = true,
                onOkClick = {
                    if (isSuccess) onConfirm() else onDismiss()
                },
                onBackClick = onDismiss,
            )
        }
    }
}

@Composable
private fun dialogMessage(state: ShopDialogState): String {
    val item = state.item
    return when (val outcome = state.outcome) {

        is PurchaseOutcome.Success -> when (item.category) {
            ItemCategory.MANDATORY -> {
                if (outcome.leveledUp) {
                    stringResource(R.string.shop_msg_level_up, outcome.newLevel)
                } else {
                    stringResource(
                        R.string.shop_msg_eat,
                        state.satietyBefore,
                        outcome.newSatiety,
                    )
                }
            }
            ItemCategory.OPTIONAL -> stringResource(R.string.shop_msg_fun)
            else -> ""
        }

        is PurchaseOutcome.NotEnoughMoney ->
            stringResource(R.string.shop_msg_no_money, outcome.shortage)

        PurchaseOutcome.AlreadyFull ->
            stringResource(R.string.shop_msg_already_full)

        PurchaseOutcome.CannotImproveMood ->
            stringResource(R.string.shop_msg_mood_max)

        is PurchaseOutcome.GoalInProgress -> stringResource(
            R.string.shop_msg_goal_progress,
            outcome.saved,
            outcome.price,
            (outcome.price - outcome.saved).coerceAtLeast(0),
        )

        PurchaseOutcome.GoalAlreadyChosen ->
            stringResource(R.string.shop_msg_goal_other)

        PurchaseOutcome.NoGoalChosen ->
            stringResource(R.string.shop_msg_goal_none)

        else -> ""
    }
}