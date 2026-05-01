package com.codeartify.membership.managing_memberships.application.activate_membership

data class ActivateMembershipRequest(
    val customerId: String,
    val planId: String,
    val signedByGuardian: Boolean = false
)
