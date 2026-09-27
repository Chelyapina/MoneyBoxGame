package com.example.onboarding.domain

object PetNameValidator {
    const val MIN = 2
    const val MAX = 30

    fun validate(raw: String): Boolean =
        raw.trim().length in MIN..MAX
}