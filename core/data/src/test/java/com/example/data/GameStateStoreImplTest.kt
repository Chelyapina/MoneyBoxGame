package com.example.data

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameStateStoreImplTest {

    private fun prefs(block: MutablePreferences.() -> Unit): Preferences =
        mutablePreferencesOf().apply(block)

    @Test
    fun `empty preferences - all defaults`() {
        val state = emptyPreferences().toGameState()
        assertNull(state.petName)
        assertFalse(state.isOnboardingDone)
        assertEquals(OwlSize.SMALL, state.owlSize)
        assertEquals(OwlMood.NEUTRAL, state.owlMood)
        assertNull(state.owlAccessory)
        assertEquals(OwlEyeColor.YELLOW, state.owlEyeColor)
    }

    @Test
    fun `pet name set - onboarding not done until flag set`() {
        val state = prefs {
            this[GameStateKeys.PET_NAME] = "Барсик"
        }.toGameState()
        assertEquals("Барсик", state.petName)
        assertFalse(state.isOnboardingDone)   // ← теперь так
    }

    @Test
    fun `onboarding flag set - onboarding done`() {
        val state = prefs {
            this[GameStateKeys.ONBOARDING_DONE] = true
        }.toGameState()
        assertTrue(state.isOnboardingDone)
    }

    @Test
    fun `all fields set - parsed correctly`() {
        val state = prefs {
            this[GameStateKeys.PET_NAME] = "Барсик"
            this[GameStateKeys.OWL_SIZE] = OwlSize.LARGE.name
            this[GameStateKeys.OWL_MOOD] = OwlMood.GOOD.name
            this[GameStateKeys.OWL_ACCESSORY] = OwlAccessory.CROWN.name
            this[GameStateKeys.OWL_EYE_COLOR] = OwlEyeColor.VIOLET.name
        }.toGameState()

        assertEquals(OwlSize.LARGE, state.owlSize)
        assertEquals(OwlMood.GOOD, state.owlMood)
        assertEquals(OwlAccessory.CROWN, state.owlAccessory)
        assertEquals(OwlEyeColor.VIOLET, state.owlEyeColor)
    }

    @Test
    fun `garbage enum value falls back to default`() {
        val state = prefs {
            this[GameStateKeys.OWL_SIZE] = "GIGANTIC"
            this[GameStateKeys.OWL_MOOD] = "SLEEPY"
        }.toGameState()

        assertEquals(OwlSize.SMALL, state.owlSize)
        assertEquals(OwlMood.NEUTRAL, state.owlMood)
    }

    @Test
    fun `normalize trims whitespace`() {
        assertEquals("Барсик", normalizePetName("  Барсик  "))
    }

    @Test
    fun `normalize rejects blank`() {
        assertNull(normalizePetName(""))
        assertNull(normalizePetName("   "))
    }
}