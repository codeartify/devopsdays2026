package com.codeartify.membership.billing

import com.codeartify.membership.managing_memberships.domain.events.MembershipActivatedEvent
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.axonframework.messaging.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class BillingEventHandler(
    private val invoiceRepository: InvoiceRepository,
    private val eventGateway: EventGateway
) {

    @EventHandler
    fun on(event: MembershipActivatedEvent) {
        val invoice = Invoice.issueFor(
            membershipId = event.membershipId,
            customerId = event.customerId,
            amount = event.planTerms.price
        )

        invoiceRepository.save(invoice)

        eventGateway.publish(
            listOf(
                InvoiceIssuedEvent(
                invoiceId = invoice.id,
                membershipId = event.membershipId,
                customerId = event.customerId,
                amount = invoice.amount,
                dueDate = invoice.dueDate
            )
            )
        )
    }
}
