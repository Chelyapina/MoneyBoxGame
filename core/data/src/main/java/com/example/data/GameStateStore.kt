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
}
class GameStateStoreImpl(private val context: Context) : GameStateStore {

    override val state: Flow<GameState> = context.gameDataStore.data
        .map { it.toGameState() }

    override suspend fun setPetName(name: String) {
        val normalized = normalizePetName(name) ?: return
        context.gameDataStore.edit { prefs ->
            prefs[GameStateKeys.PET_NAME] = normalized
        }
    }
}