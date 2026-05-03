package com.codeartify.membership.notifying_customers

import com.codeartify.membership.billing.InvoiceIssuedEvent
import com.codeartify.membership.customer_cache.CustomerCacheRepository
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.springframework.stereotype.Component

@Component
class MembershipInvoiceEmailNotificationPolicy(
) {

    /**
     *
     * # 4. Send out an email to the customer for a new InvoiceIssuedEvent
     *
     * * Use @EventHandler in MembershipInvoiceEmailNotificationPolicy to react on the event
     * * Get the customer's email from the local customer cache repository
     * * Use the EmailSender interface to send an email to the customer with the invoice details (see the interface definition to know which fields to use)
     */
}
