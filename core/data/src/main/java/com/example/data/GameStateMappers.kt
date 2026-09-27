package com.example.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal object GameStateKeys {
    val PET_NAME = stringPreferencesKey("pet_name")
    val OWL_SIZE = stringPreferencesKey("owl_size")
    val OWL_MOOD = stringPreferencesKey("owl_mood")
    val OWL_ACCESSORY = stringPreferencesKey("owl_accessory")
    val OWL_EYE_COLOR = stringPreferencesKey("owl_eye_color")
    val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
}

internal fun Preferences.toGameState(): GameState = GameState(
    petName = this[GameStateKeys.PET_NAME],
    owlSize = this[GameStateKeys.OWL_SIZE].toEnumOrDefault(OwlSize.SMALL),
    owlMood = this[GameStateKeys.OWL_MOOD].toEnumOrDefault(OwlMood.NEUTRAL),
    owlAccessory = this[GameStateKeys.OWL_ACCESSORY].toEnumOrNull<OwlAccessory>(),
    owlEyeColor = this[GameStateKeys.OWL_EYE_COLOR].toEnumOrDefault(OwlEyeColor.YELLOW),
    isOnboardingDone = this[GameStateKeys.ONBOARDING_DONE] ?: false,
)

internal fun normalizePetName(raw: String): String? =
    raw.trim().takeIf { it.isNotEmpty() }

private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T =
    this?.let { runCatching { enumValueOf<T>(it) }.getOrNull() } ?: default

private inline fun <reified T : Enum<T>> String?.toEnumOrNull(): T? =
    this?.let { runCatching { enumValueOf<T>(it) }.getOrNull() }