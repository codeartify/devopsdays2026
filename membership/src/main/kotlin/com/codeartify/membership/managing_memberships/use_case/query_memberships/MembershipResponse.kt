package com.codeartify.membership.managing_memberships.use_case.query_memberships

import java.time.LocalDate

data class MembershipResponse(
    val id: String,
    val customerId: String,
    val planId: String,
    val planDuration: Int?,
    val planPrice: Int?,
    val customerDateOfBirth: LocalDate?,
    val guardianSignaturePresent: Boolean?,
    val status: String,
    val pauseStartDate: LocalDate?,
    val pauseEndDate: LocalDate?,
    val pauseDurationDays: Int?
)
