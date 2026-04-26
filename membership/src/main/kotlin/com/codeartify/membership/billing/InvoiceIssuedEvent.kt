package com.codeartify.membership.billing

import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import org.axonframework.messaging.eventhandling.annotation.Event
import java.time.LocalDate

@Event(version = "1.0")
data class InvoiceIssuedEvent(
    val invoiceId: String,
    val membershipId: MembershipId,
    val customerId: CustomerId,
    val amount: Int,
    val dueDate: LocalDate
)
