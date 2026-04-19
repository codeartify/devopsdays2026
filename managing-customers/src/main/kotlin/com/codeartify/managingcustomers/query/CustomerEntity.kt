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

    constructor(id: String, name: String) : this() {
        this.id = id
        this.name = name
    }
}
