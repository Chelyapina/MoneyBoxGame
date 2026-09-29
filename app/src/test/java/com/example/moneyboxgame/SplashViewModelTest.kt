package com.example.moneyboxgame

import com.example.data.gameState.data.GameState
import com.example.data.gameState.domain.ObserveGameStateUseCase
import com.example.data.gameState.mock.FakeStore
import com.example.moneyboxgame.navigation.Route
import com.example.moneyboxgame.presentation.SplashViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() = Dispatchers.setMain(dispatcher)
    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `onboarding not done - go to onboarding`() = runTest(dispatcher) {
        val vm = SplashViewModel(
            ObserveGameStateUseCase(FakeStore(GameState.Empty))
        )
        advanceUntilIdle()
        assertEquals(Route.ONBOARDING, vm.startDestination.value)
    }

    @Test
    fun `onboarding done - go to home`() = runTest(dispatcher) {
        val vm = SplashViewModel(
            ObserveGameStateUseCase(
                FakeStore(GameState(petName = "Барсик", isOnboardingDone = true))
            )
        )
        advanceUntilIdle()
        assertEquals(Route.HOME, vm.startDestination.value)
    }
}