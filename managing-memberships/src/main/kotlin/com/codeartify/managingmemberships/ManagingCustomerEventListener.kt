package com.codeartify.managingmemberships

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.LocalDate

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
        customerRepository.save(CustomerEntity(event.customerId, event.name,  event.dateOfBirth))

    }
}
