package com.codeartify.membership.notifying

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ConsoleEmailSender : EmailSender {
    override fun send(to: String, invoiceId: String, amount: Int, dueDate: LocalDate) {
        println(
            """
            |
            |To: $to
            |From: billing@codeartify.com
            |Subject: Your Membership Invoice $invoiceId
            |
            |Dear customer,
            |
            |Thank you for your membership.
            |
            |Please find your invoice details below:
            |Invoice ID: $invoiceId
            |Amount Due: CHF $amount
            |Due Date: $dueDate
            |
            |Attachment: invoice-$invoiceId.pdf
            |
            |Kind regards,
            |Codeartify Billing
            |
            """.trimMargin()
        )
    }
}
