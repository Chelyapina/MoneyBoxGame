package com.example.domain.usecase

import com.example.data.gameState.data.OwlAccessory
import com.example.data.gameState.mock.FakeStore
import com.example.onboarding.domain.usecase.SaveOwlAccessoryUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SaveOwlAccessoryUseCaseTest {

    @Test
    fun `saves accessory`() = runTest {
        val store = FakeStore()
        SaveOwlAccessoryUseCase(store).invoke(OwlAccessory.CROWN)
        Assert.assertEquals(OwlAccessory.CROWN, store.savedAccessory)
    }

    @Test
    fun `null accessory does not write`() = runTest {
        val store = FakeStore()
        SaveOwlAccessoryUseCase(store).invoke(null)
        Assert.assertNull(store.savedAccessory)
    }
}