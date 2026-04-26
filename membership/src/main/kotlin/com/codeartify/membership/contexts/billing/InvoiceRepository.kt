package com.codeartify.membership.contexts.billing

import org.springframework.data.jpa.repository.JpaRepository

interface InvoiceRepository : JpaRepository<Invoice, String>
