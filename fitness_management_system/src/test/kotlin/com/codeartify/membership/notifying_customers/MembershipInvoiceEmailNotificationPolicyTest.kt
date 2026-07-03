package com.codeartify.membership.notifying_customers

import com.codeartify.membership.billing.InvoiceIssuedEvent
import com.codeartify.membership.customer_cache.CustomerCacheRepository
import com.codeartify.membership.customer_cache.CustomerEntity
import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDate
import java.util.Optional

class MembershipInvoiceEmailNotificationPolicyTest {

    @Test
    fun `invoice notification is sent to current cached customer email`() {
        val customerCacheRepository = mock(CustomerCacheRepository::class.java)
        val emailSender = RecordingEmailSender()
        `when`(customerCacheRepository.findById("customer-1")).thenReturn(
            Optional.of(
                CustomerEntity(
                    id = "customer-1",
                    name = "Updated Member",
                    email = "knowledge@codeartify.com",
                    dateOfBirth = LocalDate.of(1986, 8, 13)
                )
            )
        )

        MembershipInvoiceEmailNotificationPolicy(emailSender, customerCacheRepository).on(
            InvoiceIssuedEvent(
                invoiceId = "invoice-1",
                membershipId = MembershipId.of("membership-1"),
                customerId = CustomerId.of("customer-1"),
                amount = 139,
                dueDate = LocalDate.of(2026, 7, 31)
            )
        )

        assertEquals("knowledge@codeartify.com", emailSender.to)
        assertEquals("invoice-1", emailSender.invoiceId)
        assertEquals(139, emailSender.amount)
        assertEquals(LocalDate.of(2026, 7, 31), emailSender.dueDate)
    }

    private class RecordingEmailSender : EmailSender {
        var to: String? = null
        var invoiceId: String? = null
        var amount: Int? = null
        var dueDate: LocalDate? = null

        override fun send(to: String, invoiceId: String, amount: Int, dueDate: LocalDate) {
            this.to = to
            this.invoiceId = invoiceId
            this.amount = amount
            this.dueDate = dueDate
        }
    }
}
