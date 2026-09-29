package com.example.data.gameState.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

internal object GameStateKeys {
    val PET_NAME = stringPreferencesKey("pet_name")
    val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")

    val LEVEL = intPreferencesKey("level")
    val GOAL_ID = stringPreferencesKey("goal_id")

    val OWL_MOOD = stringPreferencesKey("owl_mood")
    val OWL_ACCESSORY = stringPreferencesKey("owl_accessory")
    val OWL_EYE_COLOR = stringPreferencesKey("owl_eye_color")

    val SATIETY = intPreferencesKey("satiety")
    val BALANCE_FOOD = intPreferencesKey("balance_food")
    val BALANCE_FUN = intPreferencesKey("balance_fun")
    val SAVINGS = intPreferencesKey("savings")

    val PURCHASES_THIS_LEVEL = stringSetPreferencesKey("purchases_this_level")
    val PLAN_CHANGES = intPreferencesKey("plan_changes")
    val PLAN_FOOD = intPreferencesKey("plan_food")
    val PLAN_FUN = intPreferencesKey("plan_fun")
    val PLAN_SAVINGS = intPreferencesKey("plan_savings")

    val SPENT_FOOD = intPreferencesKey("spent_food")
    val SPENT_FUN = intPreferencesKey("spent_fun")
}

internal fun Preferences.toGameState(): GameState = GameState(
    petName = this[GameStateKeys.PET_NAME],
    isOnboardingDone = this[GameStateKeys.ONBOARDING_DONE] ?: false,

    level = this[GameStateKeys.LEVEL] ?: 1,
    goalId = this[GameStateKeys.GOAL_ID],

    owlMood = this[GameStateKeys.OWL_MOOD].toEnumOrDefault(OwlMood.NEUTRAL),
    owlAccessory = this[GameStateKeys.OWL_ACCESSORY].toEnumOrNull<OwlAccessory>(),
    owlEyeColor = this[GameStateKeys.OWL_EYE_COLOR].toEnumOrDefault(OwlEyeColor.YELLOW),

    satiety = this[GameStateKeys.SATIETY] ?: 10,
    balanceFood = this[GameStateKeys.BALANCE_FOOD] ?: 25,
    balanceFun = this[GameStateKeys.BALANCE_FUN] ?: 25,
    savings = this[GameStateKeys.SAVINGS] ?: 0,

    purchasesThisLevel = this[GameStateKeys.PURCHASES_THIS_LEVEL]?.toList() ?: emptyList(),
    planChanges = this[GameStateKeys.PLAN_CHANGES] ?: 0,
    planFood = this[GameStateKeys.PLAN_FOOD] ?: 0,
    planFun = this[GameStateKeys.PLAN_FUN] ?: 0,
    planSavings = this[GameStateKeys.PLAN_SAVINGS] ?: 0,

    spentFood = this[GameStateKeys.SPENT_FOOD] ?: 0,
    spentFun = this[GameStateKeys.SPENT_FUN] ?: 0,
)

internal fun normalizePetName(raw: String): String? =
    raw.trim().takeIf { it.isNotEmpty() }

private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T =
    this?.let { runCatching { enumValueOf<T>(it) }.getOrNull() } ?: default

private inline fun <reified T : Enum<T>> String?.toEnumOrNull(): T? =
    this?.let { runCatching { enumValueOf<T>(it) }.getOrNull() }