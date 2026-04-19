package com.codeartify.customerservice

import com.codeartify.customerservice.presentation.CustomerResponse
import com.codeartify.customerservice.query.CustomerRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class FetchCustomerController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
    private val customerRepository: CustomerRepository
) {

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: String): ResponseEntity<CustomerResponse> {
        val customer = customerRepository.findById(id).orElseThrow()

        val response = CustomerResponse(
            id = customer.id,
            name = customer.name,
        )

        return ResponseEntity.ok(
            response
        )
    }
}

