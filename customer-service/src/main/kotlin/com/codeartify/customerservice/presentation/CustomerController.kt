package com.codeartify.customerservice

import com.codeartify.customerservice.command.RegisterCustomerCommand
import com.codeartify.customerservice.presentation.CustomerResponse
import com.codeartify.customerservice.presentation.RegisterCustomerRequest
import com.codeartify.customerservice.query.CustomerRepository
import com.codeartify.customerservice.query.GetCustomerQuery
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.util.*

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
    private val customerRepository: CustomerRepository
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
            commandGateway.sendAndWait<String>(RegisterCustomerCommand(customerId, request.name))

            val customer = subscriptionQuery.updates()
                .blockFirst(Duration.ofSeconds(5))
                ?: throw RuntimeException("Timeout waiting for customer projection")

            return ResponseEntity.ok(customer)
        }
    }

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: String): ResponseEntity<CustomerResponse> {
        val customer = customerRepository.findById(id).orElseThrow()
        return ResponseEntity.ok(
            CustomerResponse(
                id = customer.id,
                name = customer.name,
            )
        )
    }
}

