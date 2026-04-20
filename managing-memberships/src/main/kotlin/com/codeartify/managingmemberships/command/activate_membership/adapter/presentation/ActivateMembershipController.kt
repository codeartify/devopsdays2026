package com.codeartify.managingmemberships.command.activate_membership.adapter.presentation

import com.codeartify.managingmemberships.command.activate_membership.use_case.ActivateMembershipUseCase
import com.codeartify.managingmemberships.domain.CustomerId
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
        val membershipId = activateMembershipUseCase.execute(customerId)
        return ResponseEntity.ok(membershipId.value)
    }

}
