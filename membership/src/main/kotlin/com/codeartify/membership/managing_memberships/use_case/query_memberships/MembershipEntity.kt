package com.codeartify.membership.managing_memberships.use_case.query_memberships

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "memberships")
class MembershipEntity() {
    @Id
    var id: String = ""
    var customerId: String = ""
    var planId: String = ""
    var planDuration: Int? = null
    var planPrice: Int? = null
    var customerDateOfBirth: LocalDate? = null
    var guardianSignaturePresent: Boolean? = null
    var status: String = ""
    var pauseStartDate: LocalDate? = null
    var pauseEndDate: LocalDate? = null
    var pauseDurationDays: Int? = null

    constructor(
        id: String,
        customerId: String,
        planId: String,
        planDuration: Int,
        planPrice: Int,
        customerDateOfBirth: LocalDate,
        guardianSignaturePresent: Boolean,
        status: String
    ) : this() {
        this.id = id
        this.customerId = customerId
        this.planId = planId
        this.planDuration = planDuration
        this.planPrice = planPrice
        this.customerDateOfBirth = customerDateOfBirth
        this.guardianSignaturePresent = guardianSignaturePresent
        this.status = status
    }
}
