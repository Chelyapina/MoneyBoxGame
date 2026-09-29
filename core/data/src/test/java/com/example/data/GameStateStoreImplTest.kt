package com.example.data

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import com.example.data.gameState.data.GameStateKeys
import com.example.data.gameState.data.OwlAccessory
import com.example.data.gameState.data.OwlEyeColor
import com.example.data.gameState.data.OwlMood
import com.example.data.gameState.data.OwlSize
import com.example.data.gameState.data.normalizePetName
import com.example.data.gameState.data.toGameState
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

        assertEquals(1, state.level)
        assertNull(state.goalId)

        assertEquals(OwlSize.SMALL, state.owlSize)
        assertEquals(OwlMood.NEUTRAL, state.owlMood)
        assertNull(state.owlAccessory)
        assertEquals(OwlEyeColor.YELLOW, state.owlEyeColor)

        assertEquals(10, state.satiety)
        assertEquals(25, state.balanceFood)
        assertEquals(25, state.balanceFun)
        assertEquals(0, state.savings)

        assertTrue(state.purchasesThisLevel.isEmpty())
        assertEquals(0, state.planChanges)
        assertEquals(0, state.planFood)
        assertEquals(0, state.planFun)
        assertEquals(0, state.planSavings)

        assertEquals(0, state.spentFood)
        assertEquals(0, state.spentFun)

        assertEquals(50, state.coins)
    }

    @Test
    fun `pet name set - onboarding not done until flag set`() {
        val state = prefs {
            this[GameStateKeys.PET_NAME] = "Барсик"
        }.toGameState()

        assertEquals("Барсик", state.petName)
        assertFalse(state.isOnboardingDone)
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
            this[GameStateKeys.LEVEL] = 3
            this[GameStateKeys.GOAL_ID] = "goal-42"

            this[GameStateKeys.OWL_MOOD] = OwlMood.GOOD.name
            this[GameStateKeys.OWL_ACCESSORY] = OwlAccessory.CROWN.name
            this[GameStateKeys.OWL_EYE_COLOR] = OwlEyeColor.VIOLET.name

            this[GameStateKeys.SATIETY] = 80
            this[GameStateKeys.BALANCE_FOOD] = 100
            this[GameStateKeys.BALANCE_FUN] = 50
            this[GameStateKeys.SAVINGS] = 300

            this[GameStateKeys.PURCHASES_THIS_LEVEL] = setOf("hat", "glasses")
            this[GameStateKeys.PLAN_CHANGES] = 2
            this[GameStateKeys.PLAN_FOOD] = 10
            this[GameStateKeys.PLAN_FUN] = 20
            this[GameStateKeys.PLAN_SAVINGS] = 30

            this[GameStateKeys.SPENT_FOOD] = 5
            this[GameStateKeys.SPENT_FUN] = 7
        }.toGameState()

        assertEquals("Барсик", state.petName)
        assertEquals(3, state.level)
        assertEquals(OwlSize.LARGE, state.owlSize)
        assertEquals("goal-42", state.goalId)

        assertEquals(OwlMood.GOOD, state.owlMood)
        assertEquals(OwlAccessory.CROWN, state.owlAccessory)
        assertEquals(OwlEyeColor.VIOLET, state.owlEyeColor)

        assertEquals(80, state.satiety)
        assertEquals(100, state.balanceFood)
        assertEquals(50, state.balanceFun)
        assertEquals(300, state.savings)

        assertEquals(setOf("hat", "glasses"), state.purchasesThisLevel.toSet())
        assertEquals(2, state.planChanges)
        assertEquals(10, state.planFood)
        assertEquals(20, state.planFun)
        assertEquals(30, state.planSavings)

        assertEquals(5, state.spentFood)
        assertEquals(7, state.spentFun)

        assertEquals(150, state.coins)
    }

    @Test
    fun `garbage enum value falls back to default`() {
        val state = prefs {
            this[GameStateKeys.OWL_MOOD] = "SLEEPY"
        }.toGameState()

        assertEquals(OwlMood.NEUTRAL, state.owlMood)
        assertEquals(OwlSize.SMALL, state.owlSize)
    }

    @Test
    fun `owl size derived from level`() {
        assertEquals(
            OwlSize.SMALL,
            prefs { this[GameStateKeys.LEVEL] = 1 }.toGameState().owlSize,
        )
        assertEquals(
            OwlSize.MEDIUM,
            prefs { this[GameStateKeys.LEVEL] = 2 }.toGameState().owlSize,
        )
        assertEquals(
            OwlSize.LARGE,
            prefs { this[GameStateKeys.LEVEL] = 3 }.toGameState().owlSize,
        )
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