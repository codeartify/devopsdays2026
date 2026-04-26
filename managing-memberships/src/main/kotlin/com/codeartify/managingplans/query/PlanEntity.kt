package com.codeartify.managingplans.query

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "plans")
class PlanEntity() {
    @Id
    var id: String = ""
    var title: String = ""
    var description: String = ""
    var price: Int = 0
    var durationInMonths: Int = 0

    constructor(id: String, title: String, description: String, price: Int, durationInMonths: Int) : this() {
        this.id = id
        this.title = title
        this.description = description
        this.price = price
        this.durationInMonths = durationInMonths
    }
}
