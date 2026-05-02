package com.codeartify.membership.config

import jakarta.persistence.EntityManagerFactory
import org.axonframework.eventsourcing.eventstore.AnnotationBasedTagResolver
import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.eventsourcing.eventstore.StorageEngineBackedEventStore
import org.axonframework.eventsourcing.eventstore.jpa.AggregateBasedJpaEventStorageEngine
import org.axonframework.messaging.commandhandling.CommandBus
import org.axonframework.messaging.commandhandling.CommandPriorityCalculator
import org.axonframework.messaging.commandhandling.RoutingStrategy
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.commandhandling.gateway.ConvertingCommandGateway
import org.axonframework.messaging.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.messaging.core.ClassBasedMessageTypeResolver
import org.axonframework.messaging.core.MessageTypeResolver
import org.axonframework.messaging.core.annotation.AnnotationMessageTypeResolver
import org.axonframework.messaging.core.conversion.MessageConverter
import org.axonframework.messaging.core.unitofwork.transaction.jpa.JpaTransactionalExecutorProvider
import org.axonframework.messaging.eventhandling.SimpleEventBus
import org.axonframework.messaging.eventhandling.conversion.EventConverter
import org.axonframework.messaging.eventhandling.gateway.DefaultEventGateway
import org.axonframework.messaging.eventhandling.gateway.EventGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfig {
    @Bean
    fun messageTypeResolver(): MessageTypeResolver =
        AnnotationMessageTypeResolver(ClassBasedMessageTypeResolver())

    @Bean
    fun eventStorageEngine(
        entityManagerFactory: EntityManagerFactory,
        eventConverter: EventConverter
    ): EventStorageEngine = AggregateBasedJpaEventStorageEngine(
        JpaTransactionalExecutorProvider(entityManagerFactory),
        eventConverter
    ) { config -> config }

    @Bean
    fun eventStore(
        eventStorageEngine: EventStorageEngine
    ): EventStore = StorageEngineBackedEventStore(
        eventStorageEngine,
        SimpleEventBus(),
        AnnotationBasedTagResolver()
    )

    @Bean
    fun eventGateway(
        eventStore: EventStore,
        messageTypeResolver: MessageTypeResolver
    ): EventGateway = DefaultEventGateway(eventStore, messageTypeResolver)

    @Bean
    fun commandGateway(axonConfiguration: org.axonframework.common.configuration.Configuration): CommandGateway =
        ConvertingCommandGateway(
            DefaultCommandGateway(
                axonConfiguration.getComponent(CommandBus::class.java),
                axonConfiguration.getComponent(MessageTypeResolver::class.java),
                axonConfiguration.getComponent(CommandPriorityCalculator::class.java),
                axonConfiguration.getComponent(RoutingStrategy::class.java)
            ),
            axonConfiguration.getComponent(MessageConverter::class.java)
        )
}
