package com.codeartify.membership.managing_memberships.use_case.cancel_membership

import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.commands.CancelMembershipCommand
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/memberships")
class CancelMembershipController(
    private val commandGateway: CommandGateway
) {
    @DeleteMapping("/{membershipId}")
    fun cancel(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait(CancelMembershipCommand(MembershipId.of(membershipId)))
        return ResponseEntity.noContent().build()
    }
}
