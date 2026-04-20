package com.codeartify.managingmemberships.command

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/memberships")
class MembershipController(
    private val commandGateway: CommandGateway
) {

    @PostMapping("/activate")
    fun activate(): ResponseEntity<String> {
        val membershipId = UUID.randomUUID().toString()
        commandGateway.sendAndWait<String>(ActivateMembershipCommand(membershipId))
        return ResponseEntity.ok(membershipId)
    }

    @PostMapping("/{membershipId}/pause")
    fun pause(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait<String>(PauseMembershipCommand(membershipId))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{membershipId}/reactivate")
    fun reactivate(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait<String>(ReactivateMembershipCommand(membershipId))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{membershipId}/suspend")
    fun suspend(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait<String>(SuspendMembershipCommand(membershipId))
        return ResponseEntity.ok().build()
    }
}
