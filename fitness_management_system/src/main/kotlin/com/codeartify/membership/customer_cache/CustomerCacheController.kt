package com.codeartify.membership.customer_cache

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customer-cache")
class CustomerCacheController(
    private val customerCacheRepository: CustomerCacheRepository
) {

    @PostMapping
    fun backfillCustomer(@RequestBody request: CustomerCacheBackfillRequest): ResponseEntity<String> {
        customerCacheRepository.save(
            CustomerEntity(
                id = request.id,
                name = request.name,
                email = request.email,
                dateOfBirth = request.dateOfBirth
            )
        )

        return ResponseEntity.ok(request.id)
    }
}
