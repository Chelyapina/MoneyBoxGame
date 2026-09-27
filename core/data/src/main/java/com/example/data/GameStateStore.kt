package com.example.data

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
    suspend fun setOwlSize(size: OwlSize)
    suspend fun setOwlMood(mood: OwlMood)
    suspend fun setOwlAccessory(accessory: OwlAccessory)
    suspend fun setOwlEyeColor(color: OwlEyeColor)
    suspend fun completeOnboarding()
}

class GameStateStoreImpl(private val context: Context) : GameStateStore {

    override val state: Flow<GameState> = context.gameDataStore.data
        .map { it.toGameState() }

    override suspend fun setPetName(name: String) {
        val normalized = normalizePetName(name) ?: return
        context.gameDataStore.edit { it[GameStateKeys.PET_NAME] = normalized }
    }

    override suspend fun setOwlSize(size: OwlSize) {
        context.gameDataStore.edit { it[GameStateKeys.OWL_SIZE] = size.name }
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

    override suspend fun completeOnboarding() {
        context.gameDataStore.edit { it[GameStateKeys.ONBOARDING_DONE] = true }
    }
}