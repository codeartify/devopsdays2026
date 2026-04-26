package com.codeartify.membership.contexts.managingmemberships.use_cases.pause_membership

import com.codeartify.membership.contexts.managingmemberships.domain.MembershipId
import com.codeartify.membership.contexts.managingmemberships.domain.commands.PauseMembershipCommand
import org.axonframework.commandhandling.gateway.CommandGateway
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
        commandGateway.sendAndWait<String>(PauseMembershipCommand(MembershipId.of(membershipId)))
        return ResponseEntity.ok().build()
    }

}
