package com.codeartify.managingmemberships.command

import com.codeartify.managingmemberships.domain.MembershipId
import com.codeartify.managingmemberships.domain.PauseMembershipCommand
import com.codeartify.managingmemberships.domain.ReactivateMembershipCommand
import com.codeartify.managingmemberships.domain.SuspendMembershipCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/memberships")
class MembershipController(
    private val commandGateway: CommandGateway
) {

    @PostMapping("/{membershipId}/pause")
    fun pause(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait<String>(PauseMembershipCommand(MembershipId.of(membershipId)))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{membershipId}/reactivate")
    fun reactivate(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait<String>(ReactivateMembershipCommand(MembershipId.Companion.of(membershipId)))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{membershipId}/suspend")
    fun suspend(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait<String>(SuspendMembershipCommand(MembershipId.Companion.of(membershipId)))
        return ResponseEntity.ok().build()
    }
}
