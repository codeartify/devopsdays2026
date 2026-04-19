package com.codeartify.customerservice

import com.codeartify.customerservice.command.CustomerRegisteredEvent
import com.codeartify.customerservice.dto.CustomerResponse
import com.codeartify.customerservice.query.CustomerEntity
import com.codeartify.customerservice.query.CustomerRepository
import com.codeartify.customerservice.query.GetCustomerQuery
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("customer-query-processing")
class CustomerQueryHandler(
    private val customerRepository: CustomerRepository,
    private val queryUpdateEmitter: QueryUpdateEmitter
) {
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

