package com.codeartify.managingcustomers.command

import com.codeartify.managingcustomers.dto.CustomerResponse
import com.codeartify.managingcustomers.query.GetCustomerQuery
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.util.*

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
) {

    @PostMapping("/register")
    fun registerCustomer(@RequestBody request: RegisterCustomerRequest): ResponseEntity<CustomerResponse> {
        val customerId = UUID.randomUUID().toString()

        val subscriptionQuery = queryGateway.subscriptionQuery(
            GetCustomerQuery(customerId),
            ResponseTypes.instanceOf(CustomerResponse::class.java),
            ResponseTypes.instanceOf(CustomerResponse::class.java)
        )

        subscriptionQuery.use { subscriptionQuery ->
            commandGateway.send<String>(RegisterCustomerCommand(customerId, request.name))

            val customer = subscriptionQuery.updates()
                .blockFirst(Duration.ofSeconds(5))
                ?: throw RuntimeException("Timeout waiting for customer projection")

            return ResponseEntity.ok(customer)
        }
    }

}
