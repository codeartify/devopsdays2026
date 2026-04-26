package com.codeartify.membership.contexts.managingmemberships.integration

import com.codeartify.membership.contexts.managingmemberships.query.CustomerEntity
import com.codeartify.membership.contexts.managingmemberships.query.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ManagingCustomerIntegrationConsumer(
    private val objectMapper: ObjectMapper,
    private val customerRepository: CustomerRepository
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
        customerRepository.save(CustomerEntity(event.customerId, event.name, event.dateOfBirth))
    }
}
