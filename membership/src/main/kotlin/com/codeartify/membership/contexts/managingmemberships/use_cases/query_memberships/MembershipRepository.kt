package com.codeartify.membership.contexts.managingmemberships.use_cases.query_memberships

import org.springframework.data.jpa.repository.JpaRepository

interface MembershipRepository : JpaRepository<MembershipEntity, String> {
    fun existsByCustomerIdAndStatus(customerId: String, status: String): Boolean
}
