package com.codeartify.membership.managing_memberships.domain.values

import java.time.LocalDate

data class CustomerEligibility(
    val dateOfBirth: LocalDate,
    val guardianSignaturePresent: Boolean
) {
    init {
        require(!dateOfBirth.isAfter(LocalDate.now())) {
            "Customer date of birth must not be in the future"
        }
        require(!isUnderage() || guardianSignaturePresent) {
            "Guardian signature is required for customers under 18"
        }
    }

    private fun isUnderage(): Boolean =
        dateOfBirth.isAfter(LocalDate.now().minusYears(18))
}
