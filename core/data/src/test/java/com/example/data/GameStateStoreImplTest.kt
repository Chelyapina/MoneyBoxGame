package com.example.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import org.junit.Test
import org.junit.Assert.*

class GameStateStoreImplTest {

    private fun prefs(petName: String?): Preferences =
        mutablePreferencesOf().apply {
            if (petName != null) this[GameStateKeys.PET_NAME] = petName
        }

    @Test
    fun `empty preferences - onboarding not done`() {
        val state = emptyPreferences().toGameState()
        assertNull(state.petName)
        assertFalse(state.isOnboardingDone)
    }

    @Test
    fun `pet name set - onboarding done`() {
        val state = prefs("Барсик").toGameState()
        assertEquals("Барсик", state.petName)
        assertTrue(state.isOnboardingDone)
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