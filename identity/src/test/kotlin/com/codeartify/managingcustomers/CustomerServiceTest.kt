package com.codeartify.managingcustomers

import com.codeartify.managingcustomers.integration.CustomerEmailAddressChangedIntegrationEventV1
import com.codeartify.managingcustomers.integration.CustomerPublisher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import java.time.LocalDate
import java.util.Optional

class CustomerServiceTest {

    private val customerRepository = mock(CustomerRepository::class.java)
    private val customerPublisher = mock(CustomerPublisher::class.java)
    private val customerService = CustomerService(customerRepository, customerPublisher)

    @Test
    fun `updating customer publishes email address changed event when email changes`() {
        val customer = CustomerEntity(
            id = "customer-1",
            name = "New Member",
            email = "old@example.com",
            dateOfBirth = "1987-08-12"
        )

        `when`(customerRepository.findById("customer-1")).thenReturn(Optional.of(customer))
        `when`(customerRepository.save(customer)).thenReturn(customer)

        customerService.update(
            "customer-1",
            UpdateCustomerRequest(
                name = "New Member",
                email = "new@example.com",
                dateOfBirth = LocalDate.of(1987, 8, 12)
            )
        )

        val envelope = publishedEnvelope()

        assertEquals("CustomerEmailAddressChanged", envelope["type"])
        assertEquals(1, envelope["version"])

        val payload = envelope["payload"] as CustomerEmailAddressChangedIntegrationEventV1
        assertEquals("customer-1", payload.customerId)
        assertEquals("new@example.com", payload.email)
    }

    @Test
    fun `updating customer does not publish email address changed event when email stays the same`() {
        val customer = CustomerEntity(
            id = "customer-1",
            name = "New Member",
            email = "same@example.com",
            dateOfBirth = "1987-08-12"
        )

        `when`(customerRepository.findById("customer-1")).thenReturn(Optional.of(customer))
        `when`(customerRepository.save(customer)).thenReturn(customer)

        customerService.update(
            "customer-1",
            UpdateCustomerRequest(
                name = "Updated Member",
                email = "same@example.com",
                dateOfBirth = LocalDate.of(1986, 8, 13)
            )
        )

        verifyNoInteractions(customerPublisher)
    }

    private fun publishedEnvelope(): Map<*, *> {
        val keyCaptor = ArgumentCaptor.forClass(String::class.java)
        val eventCaptor = ArgumentCaptor.forClass(Any::class.java)
        verify(customerPublisher).publish(keyCaptor.capture(), eventCaptor.capture())
        assertEquals("customer-1", keyCaptor.value)
        return eventCaptor.value as Map<*, *>
    }
}
