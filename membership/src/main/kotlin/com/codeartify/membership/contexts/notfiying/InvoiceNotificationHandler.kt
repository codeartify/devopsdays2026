package com.codeartify.membership.contexts.notfiying

import com.codeartify.membership.contexts.billing.InvoiceIssuedEvent
import com.codeartify.membership.contexts.customer_cache.CustomerCacheRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.DisallowReplay
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("notfiying-customers")
class InvoiceNotificationHandler(
    private val emailSender: EmailSender,
    private val customerRepository: CustomerCacheRepository
) {

    @EventHandler
    // @DisallowReplay // use this if replay should not resend
    fun on(event: InvoiceIssuedEvent) {
        val customer = customerRepository.findById(event.customerId.value)
            .orElseThrow { IllegalStateException("Customer not found") }

        emailSender.send(
            to = customer.email,
            invoiceId = event.invoiceId,
            amount = event.amount,
            dueDate = event.dueDate
        )
    }
}
