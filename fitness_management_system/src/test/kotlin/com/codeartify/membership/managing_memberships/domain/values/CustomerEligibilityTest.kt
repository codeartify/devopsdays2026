package com.codeartify.membership.managing_memberships.domain.values

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CustomerEligibilityTest {
    @Test
    fun `accepts adult without guardian signature`() {
        assertDoesNotThrow {
            CustomerEligibility(
                dateOfBirth = LocalDate.now().minusYears(18),
                guardianSignaturePresent = false
            )
        }
    }

    @Test
    fun `rejects future date of birth`() {
        assertThrows(IllegalArgumentException::class.java) {
            CustomerEligibility(
                dateOfBirth = LocalDate.now().plusDays(1),
                guardianSignaturePresent = true
            )
        }
    }

    @Test
    fun `rejects underage customer without guardian signature`() {
        assertThrows(IllegalArgumentException::class.java) {
            CustomerEligibility(
                dateOfBirth = LocalDate.now().minusYears(17),
                guardianSignaturePresent = false
            )
        }
    }
}
