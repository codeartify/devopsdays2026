package com.codeartify.managingmemberships.activate_membership

import com.codeartify.managingmemberships.domain.ActivateMembershipCommand
import com.codeartify.managingmemberships.domain.CustomerId
import com.codeartify.managingmemberships.domain.MembershipId
import com.codeartify.managingmemberships.domain.PlanId
import com.codeartify.managingmemberships.query.CustomerEntity
import com.codeartify.managingmemberships.query.CustomerRepository
import com.codeartify.managingmemberships.query.MembershipRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ActivateMembershipUseCase(
    private val commandGateway: CommandGateway,
    private val customerRepository: CustomerRepository,
    private val membershipRepository: MembershipRepository
) {
    fun execute(customerId: CustomerId, planId: PlanId, signedByGuardian: Boolean): MembershipId {
        val customer = getCustomerOrThrow(customerId)
        checkNoActiveMembership(customerId)
        checkGuardianSignatureIfMinor(customer, signedByGuardian)

        val membershipId = MembershipId.generate()
        val activateMembershipCommand = ActivateMembershipCommand(
            membershipId,
            customerId,
            planId,
            signedByGuardian
        )

        return commandGateway.sendAndWait(
            activateMembershipCommand
        )
    }

    private fun getCustomerOrThrow(customerId: CustomerId): CustomerEntity =
        customerRepository.findById(customerId.value)
            .orElseThrow { IllegalArgumentException("Customer with ID ${customerId.value} not found") }

    private fun checkNoActiveMembership(customerId: CustomerId) {
        require(!membershipRepository.existsByCustomerIdAndStatus(customerId.value, "ACTIVE")) {
            "Customer already has an active membership"
        }
    }

    private fun checkGuardianSignatureIfMinor(customer: CustomerEntity, signedByGuardian: Boolean) {
        if (isMinor(customer)) {
            require(signedByGuardian) {
                "Guardian signature is required for customers under 18"
            }
        }
    }

    private fun isMinor(customer: CustomerEntity): Boolean =
        customer.dateOfBirth.isAfter(LocalDate.now().minusYears(18))
}
