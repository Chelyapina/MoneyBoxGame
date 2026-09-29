package com.example.shop

import org.junit.Test
import org.junit.Assert.*
import com.example.data.gameState.data.GameState
import com.example.data.gameState.data.OwlMood
import com.example.data.gameState.mock.FakeStore
import com.example.designsystem.resources.ShopItems
import com.example.shop.domain.PurchaseItemUseCase
import com.example.shop.domain.PurchaseOutcome
import kotlinx.coroutines.test.runTest

class PurchaseItemUseCaseTest {

    private val carrot = ShopItems.requireById("carrot")
    private val candy = ShopItems.requireById("candy")
    private val cake = ShopItems.requireById("cake")
    private val goalHouse = ShopItems.requireById("goal_house")


    @Test
    fun `buying food spends balanceFood and increases satiety`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(balanceFood = 25, balanceFun = 25, satiety = 5),
        )
        val outcome = PurchaseItemUseCase(store).invoke(store.state.value, carrot)

        assertTrue(outcome is PurchaseOutcome.Success)

        val state = store.state.value
        assertEquals(22, state.balanceFood)
        assertEquals(25, state.balanceFun)
        assertEquals(7, state.satiety)
        assertEquals(OwlMood.GOOD, state.owlMood)
    }

    @Test
    fun `buying food when full returns AlreadyFull`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(balanceFood = 25, satiety = 10),
        )
        val outcome = PurchaseItemUseCase(store).invoke(store.state.value, carrot)

        assertEquals(PurchaseOutcome.AlreadyFull, outcome)
        assertEquals(25, store.state.value.balanceFood)
        assertNull(store.savedPurchase)
    }

    @Test
    fun `not enough coins returns NotEnoughMoney`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(balanceFood = 1, balanceFun = 1),
        )
        val outcome = PurchaseItemUseCase(store).invoke(store.state.value, carrot)

        assertTrue(outcome is PurchaseOutcome.NotEnoughMoney)
        assertEquals(1, (outcome as PurchaseOutcome.NotEnoughMoney).shortage)
        assertNull(store.savedPurchase)
    }

    @Test
    fun `buying fun spends balanceFun and improves mood`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(
                balanceFood = 25,
                balanceFun = 25,
                owlMood = OwlMood.BAD,
            ),
        )
        val outcome = PurchaseItemUseCase(store).invoke(store.state.value, candy)

        assertTrue(outcome is PurchaseOutcome.Success)
        val state = store.state.value
        assertEquals(25, state.balanceFood)
        assertEquals(22, state.balanceFun)
        assertEquals(OwlMood.NEUTRAL, state.owlMood)
    }

    @Test
    fun `fun purchase at GOOD returns CannotImproveMood`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(balanceFun = 25, owlMood = OwlMood.GOOD),
        )
        val outcome = PurchaseItemUseCase(store).invoke(store.state.value, candy)

        assertEquals(PurchaseOutcome.CannotImproveMood, outcome)
        assertEquals(25, store.state.value.balanceFun)
        assertNull(store.savedPurchase)
    }

    @Test
    fun `eating at max satiety levels up and clears goal`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(
                satiety = 9,
                level = 1,
                balanceFood = 30,
                goalId = "goal_house",
            ),
        )
        val outcome = PurchaseItemUseCase(store).invoke(store.state.value, carrot)

        assertTrue(outcome is PurchaseOutcome.Success)
        val state = store.state.value

        assertEquals(2, state.level)
        assertNull(state.goalId)
        assertEquals(1, state.satiety)
        assertEquals(27, state.balanceFood)
        assertTrue(store.savedPurchase!!.clearGoal)
    }

    @Test
    fun `eating below max does not level up and keeps goal`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(
                satiety = 5,
                level = 1,
                balanceFood = 30,
                goalId = "goal_house",
            ),
        )
        PurchaseItemUseCase(store).invoke(store.state.value, carrot)

        val state = store.state.value
        assertEquals(1, state.level)
        assertEquals("goal_house", state.goalId)
        assertFalse(store.savedPurchase!!.clearGoal)
    }

    @Test
    fun `level up keeps savings untouched`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(
                satiety = 9,
                level = 1,
                balanceFood = 30,
                savings = 100,
            ),
        )
        PurchaseItemUseCase(store).invoke(store.state.value, carrot)

        assertEquals(100, store.state.value.savings)
    }

    @Test
    fun `buying fun never levels up even when mood at max`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(
                balanceFun = 50,
                owlMood = OwlMood.NEUTRAL,
                level = 1,
            ),
        )
        PurchaseItemUseCase(store).invoke(store.state.value, cake)

        assertEquals(1, store.state.value.level)
        assertFalse(store.savedPurchase!!.clearGoal)
    }

    @Test
    fun `tapping current goal returns GoalInProgress and does not touch store`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(goalId = "goal_house", savings = 30),
        )
        val outcome = PurchaseItemUseCase(store).invoke(store.state.value, goalHouse)

        assertTrue(outcome is PurchaseOutcome.GoalInProgress)
        val inProgress = outcome as PurchaseOutcome.GoalInProgress
        assertEquals(30, inProgress.saved)
        assertEquals(80, inProgress.price)

        assertNull(store.savedPurchase)
        assertEquals(30, store.state.value.savings)
    }

    @Test
    fun `tapping other goal returns GoalAlreadyChosen`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(goalId = "goal_tree"),
        )
        val outcome = PurchaseItemUseCase(store).invoke(store.state.value, goalHouse)

        assertEquals(PurchaseOutcome.GoalAlreadyChosen, outcome)
        assertNull(store.savedPurchase)
    }

    @Test
    fun `tapping goal when none chosen returns NoGoalChosen`() = runTest {
        val store = FakeStore(GameState.Empty)
        val outcome = PurchaseItemUseCase(store).invoke(store.state.value, goalHouse)

        assertEquals(PurchaseOutcome.NoGoalChosen, outcome)
        assertNull(store.savedPurchase)
    }

    @Test
    fun `purchases are accumulated in history`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(balanceFood = 25, balanceFun = 25, satiety = 2),
        )
        val useCase = PurchaseItemUseCase(store)

        useCase(store.state.value, carrot)
        useCase(store.state.value, candy)

        val history = store.state.value.purchasesThisLevel
        assertEquals(listOf("carrot", "candy"), history)
    }
}