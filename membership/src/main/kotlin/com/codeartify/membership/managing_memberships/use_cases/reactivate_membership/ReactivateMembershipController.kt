package com.codeartify.membership.managing_memberships.use_cases.reactivate_membership

import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.commands.ReactivateMembershipCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/memberships")
class ReactivateMembershipController(
    private val commandGateway: CommandGateway
) {
    @PostMapping("/{membershipId}/reactivate")
    fun reactivate(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait<String>(ReactivateMembershipCommand(MembershipId.of(membershipId)))
        return ResponseEntity.ok().build()
    }
}
