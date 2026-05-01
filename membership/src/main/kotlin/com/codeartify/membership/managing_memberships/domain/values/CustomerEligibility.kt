package com.codeartify.membership.managing_memberships.domain.values

import java.time.LocalDate

data class CustomerEligibilitySnapshot(
    val dateOfBirth: LocalDate,
    val wasAdultAtActivation: Boolean,
    val guardianSignaturePresent: Boolean
)
