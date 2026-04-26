package com.codeartify.membership.managingcustomers.command

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
) {

    @PostMapping("/register")
    fun registerCustomer(@RequestBody request: RegisterCustomerRequest): ResponseEntity<String> {
        val customerId = UUID.randomUUID().toString()

        val command = RegisterCustomerCommand(customerId, request.name, request.dateOfBirth)

        commandGateway.sendAndWait<String>(command)

        return ResponseEntity.ok(customerId)
    }
}
