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
import java.time.LocalDate

class ManagingCustomerIntegrationConsumerTest {

    @Test
    fun `customer updated event refreshes cached customer email`() {
        val customerCacheRepository = mock(CustomerCacheRepository::class.java)
        val objectMapper = JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .addModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build()
        val rawEvent = objectMapper.writeValueAsString(
            mapOf(
                "type" to "CustomerUpdated",
                "version" to 1,
                "payload" to mapOf(
                    "customerId" to "customer-1",
                    "name" to "Updated Member",
                    "email" to "knowledge@codeartify.com",
                    "dateOfBirth" to "1986-08-13"
                )
            )
        )

        ManagingCustomerIntegrationConsumer(objectMapper, customerCacheRepository).on(rawEvent)

        val customerCaptor = ArgumentCaptor.forClass(CustomerEntity::class.java)
        verify(customerCacheRepository).save(customerCaptor.capture())

        val savedCustomer = customerCaptor.value
        assertEquals("customer-1", savedCustomer.id)
        assertEquals("Updated Member", savedCustomer.name)
        assertEquals("knowledge@codeartify.com", savedCustomer.email)
        assertEquals(LocalDate.of(1986, 8, 13), savedCustomer.dateOfBirth)
    }
}
