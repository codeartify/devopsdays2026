package com.codeartify.managingcustomers.query

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<CustomerEntity, String>
