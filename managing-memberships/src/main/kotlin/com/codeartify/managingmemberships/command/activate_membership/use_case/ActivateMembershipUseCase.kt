package com.codeartify.managingmemberships.command.activate_membership.use_case

import com.codeartify.managingmemberships.domain.ActivateMembershipCommand
import com.codeartify.managingmemberships.domain.CustomerId
import com.codeartify.managingmemberships.domain.MembershipId
import com.codeartify.managingmemberships.query.CustomerEntity
import com.codeartify.managingmemberships.query.CustomerRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ActivateMembershipUseCase(
    private val commandGateway: CommandGateway,
    private val customerRepository: CustomerRepository
) {
    fun execute(customerId: CustomerId): MembershipId {
        checkCustomerIsOlder16(customerId)
        val membershipId = MembershipId.generate()
        commandGateway.sendAndWait<String>(ActivateMembershipCommand(membershipId, customerId))
        return membershipId
    }

    private fun checkCustomerIsOlder16(customerId: CustomerId) {
        val customer = getCustomerOrThrow(customerId)

        checkIsOlderThan16(customer)
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

}
