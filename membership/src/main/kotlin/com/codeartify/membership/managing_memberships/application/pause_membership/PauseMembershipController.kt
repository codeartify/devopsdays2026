package com.codeartify.membership.managing_memberships.application.pause_membership

import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.commands.PauseMembershipCommand
import com.codeartify.membership.managing_memberships.domain.values.PausePeriod
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/memberships")
class PauseMembershipController(
    private val commandGateway: CommandGateway
) {

    @PostMapping("/{membershipId}/pause")
    fun pause(
        @PathVariable membershipId: String,
        @RequestBody request: PauseMembershipRequest
    ): ResponseEntity<Void> {
        val startDate = LocalDate.now()
        val pausePeriod = PausePeriod(
            startDate = startDate,
            endDate = startDate.plusDays(request.durationInDays.toLong())
        )

        commandGateway.sendAndWait(PauseMembershipCommand(MembershipId.of(membershipId), pausePeriod))
        return ResponseEntity.ok().build()
    }

}
