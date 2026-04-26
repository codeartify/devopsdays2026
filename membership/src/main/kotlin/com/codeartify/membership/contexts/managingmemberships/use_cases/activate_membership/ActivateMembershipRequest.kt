package com.codeartify.membership.contexts.managingmemberships.use_cases.activate_membership

data class ActivateMembershipRequest(
    val customerId: String,
    val planId: String,
    val signedByGuardian: Boolean = false
)
