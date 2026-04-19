package com.codeartify.managingcustomers.query

import com.codeartify.managingcustomers.command.CustomerRegisteredEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("kafka-producer")
class PublishKafkaHandler private constructor(
    private val customerPublisher: CustomerPublisher
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @EventHandler
    fun publish(event: CustomerRegisteredEvent) {
        val integrationEvent = CustomerRegisteredIntegrationEventV1(event.customerId, event.name, event.dateOfBirth)
        val envelope = mapOf(
            "type" to "CustomerRegistered",
            "version" to 1,
            "payload" to integrationEvent
        )
        log.info("Publishing event: {}", envelope)

        customerPublisher.publish(event.customerId, envelope)
    }

}

