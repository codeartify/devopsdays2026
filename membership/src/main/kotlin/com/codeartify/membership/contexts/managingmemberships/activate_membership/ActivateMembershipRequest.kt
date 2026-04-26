package com.codeartify.membership.contexts.managingmemberships.activate_membership

data class ActivateMembershipRequest(
    val customerId: String,
    val planId: String,
    val signedByGuardian: Boolean = false
)
