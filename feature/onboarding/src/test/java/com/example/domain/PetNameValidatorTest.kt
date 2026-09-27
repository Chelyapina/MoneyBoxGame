package com.example.domain

import com.example.onboarding.domain.PetNameValidator
import org.junit.Test
import org.junit.Assert.*

class PetNameValidatorTest {

    @Test fun `blank rejected`() {
        assertFalse(PetNameValidator.validate(""))
        assertFalse(PetNameValidator.validate("   "))
    }

    @Test fun `one char rejected`() {
        assertFalse(PetNameValidator.validate("a"))
    }

    @Test fun `two chars ok`() {
        assertTrue(PetNameValidator.validate("ab"))
    }

    @Test fun `exactly 30 ok`() {
        assertTrue(PetNameValidator.validate("a".repeat(30)))
    }

    @Test fun `31 rejected`() {
        assertFalse(PetNameValidator.validate("a".repeat(31)))
    }

    @Test fun `trims before check`() {
        assertTrue(PetNameValidator.validate("  ab  "))
        assertFalse(PetNameValidator.validate("  a  "))
    }
}