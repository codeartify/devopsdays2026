package com.codeartify.managingmemberships.query

import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<CustomerEntity, String>
