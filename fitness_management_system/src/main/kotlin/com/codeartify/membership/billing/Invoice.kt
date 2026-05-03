package com.codeartify.membership.billing

import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.values.Price
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.*

private const val GRACE_PERIOD = 30L

@Entity
@Table(name = "invoices")
class Invoice() {
    @Id
    var id: String = ""
    var membershipId: String = ""
    var customerId: String = ""
    var amount: Int = 0
    var dueDate: LocalDate = LocalDate.now()
    @Enumerated(EnumType.STRING)
    var state: InvoiceState = InvoiceState.OPEN

    companion object {
        fun issueFor(
            membershipId: MembershipId,
            customerId: CustomerId,
            amount: Price
        ): Invoice {
            val issuedAt = LocalDate.now()
            val dueDate = issuedAt.plusDays(GRACE_PERIOD)
            require(amount.value > 0) {
                "Invoice amount must be greater than zero"
            }
            require(dueDate.isAfter(issuedAt)) {
                "Invoice due date must be in the future"
            }

            val invoice = Invoice()
            invoice.id = UUID.randomUUID().toString()
            invoice.membershipId = membershipId.value
            invoice.customerId = customerId.value
            invoice.amount = amount.value
            invoice.dueDate = dueDate
            invoice.state = InvoiceState.OPEN
            return invoice
        }
    }
}

