package com.codeartify.membership.managing_memberships.use_cases.activate_membership

import com.codeartify.membership.customer_cache.CustomerCacheRepository
import com.codeartify.membership.customer_cache.CustomerEntity
import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.commands.ActivateMembershipCommand
import com.codeartify.membership.managing_memberships.domain.values.CustomerEligibilitySnapshot
import com.codeartify.membership.managing_memberships.domain.values.PlanId
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import com.codeartify.membership.managing_memberships.use_cases.query_memberships.MembershipRepository
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ActivateMembershipUseCase(
    private val commandGateway: CommandGateway,
    private val customerCacheRepository: CustomerCacheRepository,
    private val membershipRepository: MembershipRepository,
    private val fetchPlanTerms: FetchPlanTerms
) {
    fun execute(customerId: CustomerId, planId: PlanId, signedByGuardian: Boolean): MembershipId? {
        val customer = getCustomerOrThrow(customerId)
        checkNoActiveMembership(customerId)
        checkGuardianSignatureIfMinor(customer, signedByGuardian)

        val planTerms = getPlanTermsOrThrow(planId)
        val membershipId = MembershipId.generate()

        val activateMembershipCommand = ActivateMembershipCommand(
            membershipId,
            customerId,
            planTerms,
            customerEligibilityFrom(customer, signedByGuardian)
        )

        return commandGateway.sendAndWait(activateMembershipCommand, MembershipId::class.java)    }

    private fun customerEligibilityFrom(
        customer: CustomerEntity,
        signedByGuardian: Boolean
    ): CustomerEligibilitySnapshot = CustomerEligibilitySnapshot(
        customer.dateOfBirth,
        wasAdultAtActivation(customer),
        signedByGuardian
    )

    private fun wasAdultAtActivation(customer: CustomerEntity): Boolean =
        !customer.dateOfBirth.isAfter(LocalDate.now().minusYears(18))

    private fun getPlanTermsOrThrow(planId: PlanId): PlanTerms = (fetchPlanTerms.currentTermsFor(planId)
        ?: throw IllegalArgumentException("Plan with ID ${planId.value} not found"))

    private fun getCustomerOrThrow(customerId: CustomerId): CustomerEntity =
        customerCacheRepository.findById(customerId.value)
            .orElseThrow { IllegalArgumentException("Customer with ID ${customerId.value} not found") }

    private fun checkNoActiveMembership(customerId: CustomerId) {
        // this is a simplification. With concurrent writes, we'd need to reserve a membership request, and release it if sth fails
        // Or we use a Process manager / saga to handle concurrency
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
