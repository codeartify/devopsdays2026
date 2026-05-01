package com.codeartify.membership.managing_memberships.domain.values

import java.time.LocalDate

data class CustomerEligibility(
    val dateOfBirth: LocalDate,
    val guardianSignaturePresent: Boolean
)
