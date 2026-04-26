package com.codeartify.membership.contexts.managingmemberships.application.activate_membership

data class ActivateMembershipRequest(
    val customerId: String,
    val planId: String,
    val signedByGuardian: Boolean = false
)
