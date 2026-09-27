package com.example.domain

import com.example.data.GameState
import com.example.data.GameStateStore
import com.example.onboarding.domain.SavePetNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SavePetNameUseCaseTest {

    private class FakeStore : GameStateStore {
        private val _state = MutableStateFlow(GameState.Empty)
        override val state: StateFlow<GameState> = _state

        var saved: String? = null
        override suspend fun setPetName(name: String) {
            saved = name
            _state.value = GameState(petName = name)
        }
    }

    @Test
    fun `saves trimmed name`() = runTest {
        val store = FakeStore()
        SavePetNameUseCase(store).invoke("  Барсик  ")
        assertEquals("Барсик", store.saved)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects blank`() = runTest {
        SavePetNameUseCase(FakeStore()).invoke("   ")
    }
}