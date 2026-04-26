package com.codeartify.membership.contexts.billing

import com.codeartify.membership.contexts.managingmemberships.domain.CustomerId
import com.codeartify.membership.contexts.managingmemberships.domain.MembershipId
import java.time.LocalDate

data class InvoiceIssuedEvent(
    val invoiceId: String,
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val amount: Int,
    val dueDate: LocalDate
)
