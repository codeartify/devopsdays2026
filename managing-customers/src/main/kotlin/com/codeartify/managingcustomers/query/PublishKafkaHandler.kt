package com.codeartify.managingcustomers

import com.codeartify.managingcustomers.command.CustomerRegisteredEvent
import com.codeartify.managingcustomers.query.CustomerRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
@ProcessingGroup("kafka-producer")
class PublishKafkaHandler(
    private val customerRepository: CustomerRepository,
    private val queryUpdateEmitter: QueryUpdateEmitter
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @EventHandler
    fun publish(event: CustomerRegisteredEvent) {
        log.info("Handling CustomerRegisteredEvent: {}", event)
    }

}

