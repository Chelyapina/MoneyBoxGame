package com.example.domain.usecase

import com.example.data.OwlEyeColor
import com.example.mock.FakeStore
import com.example.onboarding.domain.usecase.SaveOwlEyeColorUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SaveOwlEyeColorUseCaseTest {

    @Test
    fun `saves eye color`() = runTest {
        val store = FakeStore()
        SaveOwlEyeColorUseCase(store).invoke(OwlEyeColor.VIOLET)
        Assert.assertEquals(OwlEyeColor.VIOLET, store.savedEyeColor)
    }
}