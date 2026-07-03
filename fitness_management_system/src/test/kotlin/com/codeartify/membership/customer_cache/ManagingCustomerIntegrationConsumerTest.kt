package com.codeartify.membership.customer_cache

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDate
import java.util.Optional

class ManagingCustomerIntegrationConsumerTest {

    private val customerCacheRepository = mock(CustomerCacheRepository::class.java)
    private val consumer = ManagingCustomerIntegrationConsumer(objectMapper(), customerCacheRepository)

    @Test
    fun `customer email address changed event updates cached customer email`() {
        val dateOfBirth = LocalDate.of(1987, 8, 12)
        val cachedCustomer = CustomerEntity(
            id = "customer-1",
            name = "New Member",
            email = "old@example.com",
            dateOfBirth = dateOfBirth
        )

        `when`(customerCacheRepository.findById("customer-1")).thenReturn(Optional.of(cachedCustomer))

        consumer.on(
            """
            {
              "type": "CustomerEmailAddressChanged",
              "version": 1,
              "payload": {
                "customerId": "customer-1",
                "email": "new@example.com"
              }
            }
            """.trimIndent()
        )

        val savedCustomer = savedCustomer()
        assertEquals("customer-1", savedCustomer.id)
        assertEquals("New Member", savedCustomer.name)
        assertEquals("new@example.com", savedCustomer.email)
        assertEquals(dateOfBirth, savedCustomer.dateOfBirth)
    }

    private fun savedCustomer(): CustomerEntity {
        val customerCaptor = ArgumentCaptor.forClass(CustomerEntity::class.java)
        verify(customerCacheRepository).save(customerCaptor.capture())
        return customerCaptor.value
    }

    private fun objectMapper() =
        JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .addModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build()
}
