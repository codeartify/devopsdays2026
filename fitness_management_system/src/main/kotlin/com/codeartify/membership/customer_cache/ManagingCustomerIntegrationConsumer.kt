package com.codeartify.membership.customer_cache

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ManagingCustomerIntegrationConsumer(
    private val objectMapper: ObjectMapper,
    private val customerCacheRepository: CustomerCacheRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

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

            "CustomerEmailAddressChanged" -> {
                val payload = objectMapper.treeToValue(
                    event.payload,
                    CustomerEmailAddressChangedIntegrationEventV1::class.java
                )
                handleCustomerEmailAddressChanged(payload)
            }
        }
    }

    private fun handleCustomerRegistered(event: CustomerRegisteredIntegrationEventV1) {
        log.info("Received customer register event {}", event)
        customerCacheRepository.save(CustomerEntity(event.customerId, event.name, event.email, event.dateOfBirth))
    }

    private fun handleCustomerEmailAddressChanged(event: CustomerEmailAddressChangedIntegrationEventV1) {
        log.info("Received customer email address changed event {}", event)

        val customer = customerCacheRepository.findById(event.customerId)
            .orElseThrow { IllegalStateException("Customer with ID ${event.customerId} not found in local cache") }

        customer.email = event.email
        customerCacheRepository.save(customer)
    }
}
