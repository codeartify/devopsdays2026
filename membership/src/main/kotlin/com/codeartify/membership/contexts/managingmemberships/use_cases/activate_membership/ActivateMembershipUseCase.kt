package com.codeartify.membership.contexts.managingmemberships.application.activate_membership

import com.codeartify.membership.contexts.customer_cache.CustomerCacheRepository
import com.codeartify.membership.contexts.customer_cache.CustomerEntity
import com.codeartify.membership.contexts.managingmemberships.domain.*
import com.codeartify.membership.contexts.managingmemberships.use_cases.query_memberships.MembershipRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ActivateMembershipUseCase(
    private val commandGateway: CommandGateway,
    private val customerCacheRepository: CustomerCacheRepository,
    private val membershipRepository: MembershipRepository,
    private val fetchPlanTerms: FetchPlanTerms
) {
    fun execute(customerId: CustomerId, planId: PlanId, signedByGuardian: Boolean): MembershipId {
        val customer = getCustomerOrThrow(customerId)
        checkNoActiveMembership(customerId)
        checkGuardianSignatureIfMinor(customer, signedByGuardian)

        val planTerms = getPlanTermsOrThrow(planId)
        val membershipId = MembershipId.generate()
        val activateMembershipCommand = ActivateMembershipCommand(
            membershipId,
            customerId,
            planId,
            planTerms,
            signedByGuardian
        )

        return commandGateway.sendAndWait(
            activateMembershipCommand
        )
    }

    private fun getPlanTermsOrThrow(planId: PlanId): PlanTerms = (fetchPlanTerms.currentTermsFor(planId)
        ?: throw IllegalArgumentException("Plan with ID ${planId.value} not found"))

    private fun getCustomerOrThrow(customerId: CustomerId): CustomerEntity =
        customerCacheRepository.findById(customerId.value)
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
