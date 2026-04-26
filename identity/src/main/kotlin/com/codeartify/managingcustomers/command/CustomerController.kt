package com.codeartify.managingcustomers.command

import com.codeartify.managingcustomers.CustomerService
import com.codeartify.managingcustomers.query.CustomerResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @PostMapping
    fun createCustomer(@RequestBody request: RegisterCustomerRequest): ResponseEntity<String> {
        val customerId = customerService.create(request)
        return ResponseEntity.ok(customerId)
    }

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: String): ResponseEntity<CustomerResponse> =
        ResponseEntity.ok(customerService.getById(id))

    @GetMapping
    fun getCustomers(): ResponseEntity<List<CustomerResponse>> =
        ResponseEntity.ok(customerService.getAll())

    @PutMapping("/{id}")
    fun updateCustomer(
        @PathVariable id: String,
        @RequestBody request: UpdateCustomerRequest
    ): ResponseEntity<CustomerResponse> = ResponseEntity.ok(customerService.update(id, request))

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: String): ResponseEntity<Void> {
        customerService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
