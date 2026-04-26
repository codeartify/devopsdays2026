package com.codeartify.membership.contexts.managingmemberships.query

import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<CustomerEntity, String>
