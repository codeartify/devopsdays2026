package com.codeartify.managingmemberships.command

import com.codeartify.managingmemberships.query.CustomerEntity
import com.codeartify.managingmemberships.query.CustomerRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/memberships")
class MembershipController(
    private val commandGateway: CommandGateway,
    private val customerRepository: CustomerRepository
) {

    @PostMapping("/activate")
    fun activate(@RequestBody request: ActivateMembershipRequest): ResponseEntity<String> {
        val membershipId = activateMembership(request)
        return ResponseEntity.ok(membershipId.value)
    }

    private fun activateMembership(request: ActivateMembershipRequest): MembershipId {
        val customerId = CustomerId.of(request.customerId)
        val customer = getCustomerOrThrow(customerId)

        checkIsOlderThan16(customer)

        val membershipId = MembershipId.generate()
        commandGateway.sendAndWait<String>(ActivateMembershipCommand(membershipId, customerId))
        return membershipId
    }

    private fun getCustomerOrThrow(customerId: CustomerId): CustomerEntity? =
        customerRepository.findById(customerId.value)
            .orElseThrow { IllegalArgumentException("Customer with ID ${customerId.value} not found") }

    private fun checkIsOlderThan16(customer: CustomerEntity?) {
        require(isOlderThan16(customer)) {
            "Customer must be at least 16 years old to activate a membership"
        }
    }

    private fun isOlderThan16(customer: CustomerEntity?): Boolean =
        customer?.dateOfBirth?.isBefore(LocalDate.now().minusYears(16).plusDays(1)) ?: false

    @PostMapping("/{membershipId}/pause")
    fun pause(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait<String>(PauseMembershipCommand(MembershipId.of(membershipId)))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{membershipId}/reactivate")
    fun reactivate(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait<String>(ReactivateMembershipCommand(MembershipId.of(membershipId)))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{membershipId}/suspend")
    fun suspend(@PathVariable membershipId: String): ResponseEntity<Void> {
        commandGateway.sendAndWait<String>(SuspendMembershipCommand(MembershipId.of(membershipId)))
        return ResponseEntity.ok().build()
    }
}
