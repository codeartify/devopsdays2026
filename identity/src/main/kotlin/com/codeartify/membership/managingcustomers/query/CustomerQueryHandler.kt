package com.codeartify.membership.managingcustomers.query

import com.codeartify.managingcustomers.event.CustomerRegisteredEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.stereotype.Component


@Component
@ProcessingGroup("customer-projection")
class CustomerQueryHandler(
    private val customerRepository: CustomerRepository,
    private val queryUpdateEmitter: QueryUpdateEmitter
) {

    @EventHandler
    fun on(evt: CustomerRegisteredEvent) {
        val customerEntity = customerRepository.save(CustomerEntity(evt.customerId, evt.name, evt.dateOfBirth.toString()))

        val response = CustomerResponse(
            id = customerEntity.id,
            name = customerEntity.name,
            dateOfBirth = customerEntity.dateOfBirth
        )

        // informs subscription queries about persisted changes
        queryUpdateEmitter.emit(GetCustomerQuery::class.java, { it.customerId == evt.customerId }, response)
    }

    @QueryHandler
    fun handle(query: GetCustomerQuery): CustomerResponse? {
        return customerRepository
            .findById(query.customerId)
            .map {entity ->
                CustomerResponse(
                    id = entity.id,
                    name = entity.name,
                    dateOfBirth = entity.dateOfBirth
                )
            }
            .orElse(null)
    }
}

