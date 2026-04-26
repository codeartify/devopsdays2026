package com.codeartify.membership.managingmemberships.query

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "memberships")
class MembershipEntity() {
    @Id
    var id: String = ""
    var customerId: String = ""
    var planId: String = ""
    var status: String = ""

    constructor(id: String, customerId: String, planId: String, status: String) : this() {
        this.id = id
        this.customerId = customerId
        this.planId = planId
        this.status = status
    }
}
