package com.codeartify.membership.managing_memberships.use_cases.pause_membership

import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.commands.PauseMembershipCommand
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/memberships")
class PauseMembershipController(
    private val commandGateway: CommandGateway
) {

    @PostMapping("/{membershipId}/pause")
    fun pause(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait(PauseMembershipCommand(MembershipId.of(membershipId)))
        return ResponseEntity.ok().build()
    }

}
