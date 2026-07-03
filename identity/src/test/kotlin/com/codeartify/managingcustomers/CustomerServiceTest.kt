package com.codeartify.managingcustomers

import com.codeartify.managingcustomers.integration.CustomerPublisher
import com.codeartify.managingcustomers.integration.CustomerUpdatedIntegrationEventV1
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDate
import java.util.Optional

class CustomerServiceTest {

    @Test
    fun `updating customer publishes customer updated integration event`() {
        val customerId = "customer-1"
        val customerRepository = mock(CustomerRepository::class.java)
        val customerPublisher = mock(CustomerPublisher::class.java)
        val customer = CustomerEntity(
            id = customerId,
            name = "New Member",
            email = "info@codeartify.com",
            dateOfBirth = "1987-08-12"
        )

        `when`(customerRepository.findById(customerId)).thenReturn(Optional.of(customer))
        `when`(customerRepository.save(any(CustomerEntity::class.java))).thenAnswer { invocation ->
            invocation.getArgument<CustomerEntity>(0)
        }

        val response = CustomerService(customerRepository, customerPublisher).update(
            customerId,
            UpdateCustomerRequest(
                name = "Updated Member",
                email = "knowledge@codeartify.com",
                dateOfBirth = LocalDate.of(1986, 8, 13)
            )
        )

        assertEquals("knowledge@codeartify.com", response.email)

        val eventCaptor = ArgumentCaptor.forClass(Any::class.java)
        verify(customerPublisher).publish(eq(customerId), eventCaptor.capture())

        val envelope = eventCaptor.value as Map<*, *>
        assertEquals("CustomerUpdated", envelope["type"])
        assertEquals(1, envelope["version"])

        val payload = envelope["payload"] as CustomerUpdatedIntegrationEventV1
        assertEquals(customerId, payload.customerId)
        assertEquals("Updated Member", payload.name)
        assertEquals("knowledge@codeartify.com", payload.email)
        assertEquals(LocalDate.of(1986, 8, 13), payload.dateOfBirth)
    }
}
