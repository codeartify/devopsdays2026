package com.codeartify.membership.contexts.notfiying

import java.time.LocalDate

interface EmailSender {
    fun send(to: String, invoiceId: String, amount: Int, dueDate: LocalDate)
}
