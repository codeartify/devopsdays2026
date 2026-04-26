package com.codeartify.membership.contexts.customer_cache

import org.springframework.data.jpa.repository.JpaRepository

interface CustomerCacheRepository : JpaRepository<CustomerEntity, String>
