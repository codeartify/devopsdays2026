package com.codeartify.membership.managing_memberships.use_case.activate_membership

data class ActivateMembershipRequest(
    val customerId: String,
    val planId: String,
    val signedByGuardian: Boolean = false
)
