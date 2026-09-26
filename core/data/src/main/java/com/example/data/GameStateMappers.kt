package com.example.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

internal object GameStateKeys {
    val PET_NAME = stringPreferencesKey("pet_name")
}

internal fun Preferences.toGameState(): GameState =
    GameState(petName = this[GameStateKeys.PET_NAME])

internal fun normalizePetName(raw: String): String? =
    raw.trim().takeIf { it.isNotEmpty() }