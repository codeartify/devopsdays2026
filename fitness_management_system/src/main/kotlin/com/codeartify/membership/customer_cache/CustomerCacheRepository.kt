package com.codeartify.membership.customer_cache

import org.springframework.data.jpa.repository.JpaRepository

interface CustomerCacheRepository : JpaRepository<CustomerEntity, String>
