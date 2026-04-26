package com.codeartify.membership.contexts.customer_cache

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ManagingCustomerIntegrationConsumer(
    private val objectMapper: ObjectMapper,
    private val customerCacheRepository: CustomerCacheRepository
) {

    @KafkaListener(topics = ["\${app.kafka.topics.managing-customer}"])
    fun on(raw: String) {
        val event = objectMapper.readValue(raw, IntegrationEvent::class.java)

        when (event.type) {
            "CustomerRegistered" -> {
                val payload = objectMapper.treeToValue(
                    event.payload,
                    CustomerRegisteredIntegrationEventV1::class.java
                )
                handleCustomerRegistered(payload)
            }
        }
    }

    private fun handleCustomerRegistered(event: CustomerRegisteredIntegrationEventV1) {
        customerCacheRepository.save(CustomerEntity(event.customerId, event.name, event.email, event.dateOfBirth))
    }
}
