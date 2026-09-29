package com.example.home.domain

import com.example.data.gameState.data.GameState
import com.example.data.gameState.mock.FakeStore
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SelectGoalUseCaseTest {

    @Test
    fun `saves goal id`() = runTest {
        val store = FakeStore()
        SelectGoalUseCase(store).invoke("goal_house")
        Assert.assertEquals("goal_house", store.savedGoalId)
    }

    @Test
    fun `updates state with goal id`() = runTest {
        val store = FakeStore()
        SelectGoalUseCase(store).invoke("goal_tree")
        Assert.assertEquals("goal_tree", store.state.value.goalId)
    }

    @Test
    fun `saves each goal from catalog`() = runTest {
        listOf("goal_house", "goal_tree", "goal_headphones").forEach { id ->
            val store = FakeStore()
            SelectGoalUseCase(store).invoke(id)
            Assert.assertEquals(id, store.savedGoalId)
        }
    }

    @Test
    fun `second call overwrites first`() = runTest {
        val store = FakeStore()
        val useCase = SelectGoalUseCase(store)
        useCase("goal_house")
        useCase("goal_tree")
        Assert.assertEquals("goal_tree", store.savedGoalId)
        Assert.assertEquals("goal_tree", store.state.value.goalId)
    }

    @Test
    fun `does not touch other fields`() = runTest {
        val store = FakeStore(
            GameState.Empty.copy(
                petName = "Барсик",
                satiety = 42,
                balanceFood = 100,
            ),
        )
        SelectGoalUseCase(store).invoke("goal_house")

        val state = store.state.value
        Assert.assertEquals("Барсик", state.petName)
        Assert.assertEquals(42, state.satiety)
        Assert.assertEquals(100, state.balanceFood)
        Assert.assertEquals("goal_house", state.goalId)
    }

    @Test
    fun `empty goal id is still written`() = runTest {
        val store = FakeStore()
        SelectGoalUseCase(store).invoke("")
        Assert.assertEquals("", store.savedGoalId)
    }

    @Test
    fun `null is not possible via signature but state stays null by default`() = runTest {
        val store = FakeStore()
        Assert.assertNull(store.state.value.goalId)
    }
}