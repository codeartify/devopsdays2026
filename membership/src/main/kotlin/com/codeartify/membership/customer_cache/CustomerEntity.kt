package com.codeartify.membership.customer_cache

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "customers")
class CustomerEntity() {
    @Id
    var id: String = ""
    var name: String = ""
    var email: String = ""
    var dateOfBirth: LocalDate = LocalDate.now()

    constructor(id: String, name: String, email: String, dateOfBirth: LocalDate) : this() {
        this.id = id
        this.name = name
        this.email = email
        this.dateOfBirth = dateOfBirth
    }
}
