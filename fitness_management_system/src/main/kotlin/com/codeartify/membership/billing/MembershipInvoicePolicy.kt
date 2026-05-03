package com.codeartify.membership.billing

import com.codeartify.membership.managing_memberships.domain.events.MembershipActivatedEvent
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.axonframework.messaging.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class MembershipInvoicePolicy(
    private val invoiceRepository: InvoiceRepository,
    private val eventGateway: EventGateway
) {

    /**
     * TODO: 3 React to the ActivateMembershipEvent
     *
     * * Use @EventHandler on the methods that should react on the event
     *
     * Create an invoice, store it in the local invoice repository, and raise a "InvoiceIssuedEvent" using Axon's eventGateway.
     * What data is needed to create the invoice (and what data is needed for the email sent by the notifications bounded context)?
     */
    @EventHandler
    fun on(event: MembershipActivatedEvent) {

        invoiceRepository.save(Invoice())


    }
}
