package com.codeartify.membership.billing

import org.axonframework.messaging.eventhandling.annotation.Event

@Event(version = "1.0")
data class InvoiceIssuedEvent(
    val invoiceId: String
)
