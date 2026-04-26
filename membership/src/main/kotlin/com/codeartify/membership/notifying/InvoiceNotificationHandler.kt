package com.codeartify.membership.notifying

import com.codeartify.membership.billing.InvoiceIssuedEvent
import com.codeartify.membership.customer_cache.CustomerCacheRepository
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.springframework.stereotype.Component

@Component
class InvoiceNotificationHandler(
    private val emailSender: EmailSender,
    private val customerRepository: CustomerCacheRepository
) {

    @EventHandler
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
