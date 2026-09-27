package com.example.domain.usecase

import com.example.mock.FakeStore
import com.example.onboarding.domain.usecase.SavePetNameUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SavePetNameUseCaseTest {

    @Test
    fun `saves trimmed name`() = runTest {
        val store = FakeStore()
        SavePetNameUseCase(store).invoke("  Барсик  ")
        assertEquals("Барсик", store.savedName)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects blank`() = runTest {
        SavePetNameUseCase(FakeStore()).invoke("   ")
    }
}