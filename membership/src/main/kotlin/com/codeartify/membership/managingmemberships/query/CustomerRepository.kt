package com.codeartify.membership.managingmemberships.query

import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<CustomerEntity, String>
