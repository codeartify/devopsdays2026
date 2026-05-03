package com.codeartify.membership.config

import org.axonframework.extension.spring.config.EventProcessorDefinition
import org.axonframework.messaging.eventhandling.processing.streaming.pooled.PooledStreamingEventProcessorConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventProcessorConfig {

    @Bean
    fun billingProcessorDefinition(): EventProcessorDefinition =
        EventProcessorDefinition.pooledStreaming("membership-invoice-policy")
            .assigningHandlers { descriptor ->
                descriptor.beanType()?.packageName == "com.codeartify.membership.billing"
            }

    @Bean
    fun membershipProjectionProcessorDefinition(): EventProcessorDefinition =
        EventProcessorDefinition.pooledStreaming("membership-projection")
            .assigningHandlers { descriptor ->
                descriptor.beanType()?.name == "com.codeartify.membership.managing_memberships.use_case.query_memberships.MembershipProjection"
            }
    @Bean
    fun notifyingCustomersProjectionProcessorDefinition(): EventProcessorDefinition =
        EventProcessorDefinition.pooledStreaming("notifying-customers")
            .assigningHandlers { descriptor ->
                descriptor.beanType()?.packageName == "com.codeartify.membership.notifying_customers"
            }
            .customized(::startAtLatestToken)

    private fun startAtLatestToken(
        config: PooledStreamingEventProcessorConfiguration
    ): PooledStreamingEventProcessorConfiguration =
        config.initialToken { tokenSource -> tokenSource.latestToken(null) }
}
