package com.codeartify.managingcustomers.query

import com.codeartify.managingcustomers.command.CustomerRegisteredEvent
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
@ProcessingGroup("kafka-producer")
class PublishKafkaHandler(
    private val commandGateway: CommandGateway
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @EventHandler
    fun publish(event: CustomerRegisteredEvent) {
        log.info("Handling CustomerRegisteredEvent: {}", event)
    }

}

