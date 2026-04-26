package com.codeartify.membership.managing_memberships.use_cases.suspend_membership

import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.commands.SuspendMembershipCommand
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/memberships")
class SuspendMembershipController(
    private val commandGateway: CommandGateway
) {

    @PostMapping("/{membershipId}/suspend")
    fun suspend(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait(SuspendMembershipCommand(MembershipId.of(membershipId)))
        return ResponseEntity.ok().build()
    }
}
