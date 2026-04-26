package com.codeartify.membership

import org.axonframework.extension.spring.config.EventProcessorDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventProcessorConfig {

    @Bean
    fun billingProcessorDefinition(): EventProcessorDefinition =
        EventProcessorDefinition.pooledStreaming("billing")
            .assigningHandlers { descriptor ->
                descriptor.beanType().packageName == "com.codeartify.membership.billing"
            }
            .notCustomized()
}
