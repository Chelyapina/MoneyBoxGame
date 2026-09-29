package com.example.data.gameState.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.gameDataStore: DataStore<Preferences>
        by preferencesDataStore(name = "game_state")

interface GameStateStore {
    val state: Flow<GameState>
    suspend fun setPetName(name: String)
    suspend fun setOwlMood(mood: OwlMood)
    suspend fun setOwlAccessory(accessory: OwlAccessory)
    suspend fun setOwlEyeColor(color: OwlEyeColor)
    suspend fun setGoal(goalId: String)
    suspend fun setPlan(food: Int, funAmount: Int, savings: Int)
    suspend fun applyPurchase(
        itemId: String,
        balanceFood: Int,
        balanceFun: Int,
        satiety: Int,
        owlMood: OwlMood,
        level: Int,
        clearGoal: Boolean,
    )
    suspend fun completeOnboarding()
}

class GameStateStoreImpl(private val context: Context) : GameStateStore {

    override val state: Flow<GameState> = context.gameDataStore.data
        .map { it.toGameState() }

    override suspend fun setPetName(name: String) {
        val normalized = normalizePetName(name) ?: return
        context.gameDataStore.edit { it[GameStateKeys.PET_NAME] = normalized }
    }

    override suspend fun setOwlMood(mood: OwlMood) {
        context.gameDataStore.edit { it[GameStateKeys.OWL_MOOD] = mood.name }
    }

    override suspend fun setOwlAccessory(accessory: OwlAccessory) {
        context.gameDataStore.edit { it[GameStateKeys.OWL_ACCESSORY] = accessory.name }
    }

    override suspend fun setOwlEyeColor(color: OwlEyeColor) {
        context.gameDataStore.edit { it[GameStateKeys.OWL_EYE_COLOR] = color.name }
    }

    override suspend fun setGoal(goalId: String) {
        context.gameDataStore.edit { prefs ->
            prefs[GameStateKeys.GOAL_ID] = goalId
        }
    }

    override suspend fun setPlan(food: Int, funAmount: Int, savings: Int) {
        context.gameDataStore.edit { prefs ->
            prefs[GameStateKeys.PLAN_FOOD] = food
            prefs[GameStateKeys.PLAN_FUN] = funAmount
            prefs[GameStateKeys.PLAN_SAVINGS] = savings
            prefs[GameStateKeys.BALANCE_FOOD] = food
            prefs[GameStateKeys.BALANCE_FUN] = funAmount
            prefs[GameStateKeys.SAVINGS] = savings
        }
    }

    override suspend fun applyPurchase(
        itemId: String,
        balanceFood: Int,
        balanceFun: Int,
        satiety: Int,
        owlMood: OwlMood,
        level: Int,
        clearGoal: Boolean,
    ) {
        context.gameDataStore.edit { prefs ->
            prefs[GameStateKeys.BALANCE_FOOD] = balanceFood
            prefs[GameStateKeys.BALANCE_FUN] = balanceFun
            prefs[GameStateKeys.SATIETY] = satiety
            prefs[GameStateKeys.OWL_MOOD] = owlMood.name
            prefs[GameStateKeys.LEVEL] = level

            if (clearGoal) {
                prefs.remove(GameStateKeys.GOAL_ID)
                prefs[GameStateKeys.PURCHASES_THIS_LEVEL] = emptySet()
            }

            val current = prefs[GameStateKeys.PURCHASES_THIS_LEVEL].orEmpty()
            prefs[GameStateKeys.PURCHASES_THIS_LEVEL] = current + itemId
        }
    }

    override suspend fun completeOnboarding() {
        context.gameDataStore.edit { it[GameStateKeys.ONBOARDING_DONE] = true }
    }
}