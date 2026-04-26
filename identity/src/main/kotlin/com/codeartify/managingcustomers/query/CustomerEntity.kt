package com.codeartify.managingcustomers.query

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "customers")
class CustomerEntity() {
    @Id
    var id: String = ""
    var name: String = ""
    var dateOfBirth: String = ""

    constructor(id: String, name: String, dateOfBirth: String) : this() {
        this.id = id
        this.name = name
        this.dateOfBirth = dateOfBirth
    }
}
