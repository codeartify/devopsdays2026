package com.codeartify.membership.managing_memberships.use_cases.activate_membership

import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.values.PlanId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/memberships")
class ActivateMembershipController(
    private val activateMembershipUseCase: ActivateMembershipUseCase
) {

    @PostMapping("/activate")
    fun activate(@RequestBody request: ActivateMembershipRequest): ResponseEntity<String> {
        val customerId = CustomerId.of(request.customerId)
        val planId = PlanId.of(request.planId)
        val membershipId = activateMembershipUseCase.execute(customerId, planId, request.signedByGuardian)
        return ResponseEntity.ok(membershipId.value)
    }

}
