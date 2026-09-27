package com.example.moneyboxgame

import com.example.data.GameState
import com.example.data.GameStateStore
import com.example.moneyboxgame.domain.ObserveGameStateUseCase
import com.example.moneyboxgame.navigation.Route
import com.example.moneyboxgame.presentation.SplashViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private class FakeStore(initial: GameState) : GameStateStore {
        private val flow = MutableStateFlow(initial)
        override val state: StateFlow<GameState> = flow
        override suspend fun setPetName(name: String) {
            flow.value = GameState(petName = name)
        }
    }

    @Before
    fun setUp() = Dispatchers.setMain(dispatcher)
    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `onboarding not done - go to onboarding`() = runTest(dispatcher) {
        val vm = SplashViewModel(ObserveGameStateUseCase(FakeStore(GameState.Empty)))
        advanceUntilIdle()
        assertEquals(Route.ONBOARDING, vm.startDestination.value)
    }

    @Test
    fun `onboarding done - go to home`() = runTest(dispatcher) {
        val vm = SplashViewModel(
            ObserveGameStateUseCase(FakeStore(GameState(petName = "Барсик")))
        )
        advanceUntilIdle()
        assertEquals(Route.HOME, vm.startDestination.value)
    }
}