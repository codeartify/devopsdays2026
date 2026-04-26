package com.codeartify.membership.billing

import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.values.PlanPrice
import jakarta.persistence.Entity
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

    companion object {
        fun issueFor(
            membershipId: MembershipId,
            customerId: CustomerId,
            amount: PlanPrice
        ): Invoice {
            val invoice = Invoice()
            invoice.id = UUID.randomUUID().toString()
            invoice.membershipId = membershipId.value
            invoice.customerId = customerId.value
            invoice.amount = amount.value
            invoice.dueDate = LocalDate.now().plusDays(GRACE_PERIOD)
            return invoice
        }
    }
}
