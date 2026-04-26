package com.codeartify.membership

import jakarta.persistence.EntityManagerFactory
import org.axonframework.eventsourcing.eventstore.AnnotationBasedTagResolver
import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.eventsourcing.eventstore.StorageEngineBackedEventStore
import org.axonframework.eventsourcing.eventstore.jpa.AggregateBasedJpaEventStorageEngine
import org.axonframework.messaging.core.unitofwork.transaction.jpa.JpaTransactionalExecutorProvider
import org.axonframework.messaging.eventhandling.conversion.EventConverter
import org.axonframework.messaging.eventhandling.SimpleEventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfig {

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
}
