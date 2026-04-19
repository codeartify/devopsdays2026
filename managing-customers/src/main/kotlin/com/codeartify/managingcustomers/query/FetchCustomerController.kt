package com.codeartify.managingcustomers

import com.codeartify.managingcustomers.dto.CustomerResponse
import com.codeartify.managingcustomers.query.GetCustomerQuery
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/customers")
class FetchCustomerController(
    private val queryGateway: QueryGateway
) {

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: String): CompletableFuture<CustomerResponse?>? {
        return queryGateway.query(GetCustomerQuery(id), CustomerResponse::class.java)
    }
}

