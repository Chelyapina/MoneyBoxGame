package com.example.savings

import com.example.data.gameState.data.GameState
import com.example.data.gameState.mock.FakeStore
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*


class SavePlanUseCaseTest {

    @Test
    fun `saves plan values`() = runTest {
        val store = FakeStore()
        SavePlanUseCase(store).invoke(20, 15, 15)

        val state = store.state.value
        assertEquals(20, state.planFood)
        assertEquals(15, state.planFun)
        assertEquals(15, state.planSavings)
    }

    @Test
    fun `applies plan to balances`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(balanceFood = 25, balanceFun = 25, savings = 0),
        )
        SavePlanUseCase(store).invoke(10, 20, 20)

        val state = store.state.value
        assertEquals(10, state.balanceFood)
        assertEquals(20, state.balanceFun)
        assertEquals(20, state.savings)
    }

    @Test
    fun `total is preserved`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(balanceFood = 25, balanceFun = 25, savings = 0),
        )
        val totalBefore = store.state.value.coins + store.state.value.savings

        SavePlanUseCase(store).invoke(10, 20, 20)

        val state = store.state.value
        val totalAfter = state.balanceFood + state.balanceFun + state.savings
        assertEquals(totalBefore, totalAfter)
    }

    @Test
    fun `coins recalculated after save`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(balanceFood = 25, balanceFun = 25),
        )
        SavePlanUseCase(store).invoke(10, 20, 20)

        // coins = balanceFood + balanceFun
        assertEquals(30, store.state.value.coins)
    }

    @Test
    fun `does not touch other fields`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(petName = "Барсик", satiety = 42, level = 2),
        )
        SavePlanUseCase(store).invoke(10, 20, 20)

        val state = store.state.value
        assertEquals("Барсик", state.petName)
        assertEquals(42, state.satiety)
        assertEquals(2, state.level)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `negative food throws`() = runTest {
        SavePlanUseCase(FakeStore()).invoke(-1, 0, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `negative fun throws`() = runTest {
        SavePlanUseCase(FakeStore()).invoke(0, -1, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `negative savings throws`() = runTest {
        SavePlanUseCase(FakeStore()).invoke(0, 0, -1)
    }

    @Test
    fun `zero values are allowed`() = runTest {
        val store = FakeStore()
        SavePlanUseCase(store).invoke(0, 0, 0)

        val state = store.state.value
        assertEquals(0, state.planFood)
        assertEquals(0, state.planFun)
        assertEquals(0, state.planSavings)
    }

    @Test
    fun `second save overwrites first`() = runTest {
        val store = FakeStore()
        val useCase = SavePlanUseCase(store)

        useCase(20, 15, 15)
        useCase(5, 5, 40)

        val state = store.state.value
        assertEquals(5, state.planFood)
        assertEquals(5, state.planFun)
        assertEquals(40, state.planSavings)
        assertEquals(5, state.balanceFood)
        assertEquals(5, state.balanceFun)
        assertEquals(40, state.savings)
    }
}