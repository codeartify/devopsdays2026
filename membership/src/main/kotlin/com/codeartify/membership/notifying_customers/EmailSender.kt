package com.codeartify.membership.notifying_customers

import java.time.LocalDate

interface EmailSender {
    fun send(to: String, invoiceId: String, amount: Int, dueDate: LocalDate)
}
