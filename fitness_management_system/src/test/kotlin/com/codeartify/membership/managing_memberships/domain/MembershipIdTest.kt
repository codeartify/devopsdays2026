package com.codeartify.membership.managing_memberships.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MembershipIdTest {
    @Test
    fun `renders as raw value for flat persistence identifiers`() {
        val value = "b84333b2-5ed9-4488-a0d7-edee5110bc20"

        assertEquals(value, MembershipId.of(value).toString())
    }
}
