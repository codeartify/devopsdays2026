package com.codeartify.membership.contexts.billing

import com.codeartify.membership.contexts.managingmemberships.domain.events.MembershipActivatedEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("membership-customer-billing")
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
            InvoiceIssuedEvent(
                invoiceId = invoice.id,
                membershipId = event.membershipId,
                customerId = event.customerId,
                amount = invoice.amount,
                dueDate = invoice.dueDate
            )
        )
    }
}
