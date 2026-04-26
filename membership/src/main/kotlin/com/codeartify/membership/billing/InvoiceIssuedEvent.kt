package com.codeartify.membership.billing

import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import java.time.LocalDate

data class InvoiceIssuedEvent(
    val invoiceId: String,
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val amount: Int,
    val dueDate: LocalDate
)
