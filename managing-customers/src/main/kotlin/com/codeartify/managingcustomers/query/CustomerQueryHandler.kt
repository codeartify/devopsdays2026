package com.codeartify.managingcustomers

import com.codeartify.managingcustomers.command.CustomerRegisteredEvent
import com.codeartify.managingcustomers.dto.CustomerResponse
import com.codeartify.managingcustomers.query.CustomerEntity
import com.codeartify.managingcustomers.query.CustomerRepository
import com.codeartify.managingcustomers.query.GetCustomerQuery
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.math.log


@Component
@ProcessingGroup("customer-query-processing")
class CustomerQueryHandler(
    private val customerRepository: CustomerRepository,
    private val queryUpdateEmitter: QueryUpdateEmitter
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @EventHandler
    fun on(evt: CustomerRegisteredEvent) {
        val customerEntity = customerRepository.save(CustomerEntity(evt.customerId, evt.name))

        val response = CustomerResponse(
            id = customerEntity.id,
            name = customerEntity.name,
        )

        // informs subscription queries about persisted changes
        queryUpdateEmitter.emit(GetCustomerQuery::class.java, { it.customerId == evt.customerId }, response)
    }

    @QueryHandler
    fun handle(query: GetCustomerQuery): CustomerResponse? {
        return customerRepository
            .findById(query.customerId)
            .map {
                CustomerResponse(
                    id = it.id,
                    name = it.name,
                )
            }
            .orElse(null)
    }
}

