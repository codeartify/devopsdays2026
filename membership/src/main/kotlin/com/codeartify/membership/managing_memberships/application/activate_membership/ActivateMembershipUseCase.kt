package com.codeartify.membership.managing_memberships.application.activate_membership

import com.codeartify.membership.customer_cache.CustomerCacheRepository
import com.codeartify.membership.customer_cache.CustomerEntity
import com.codeartify.membership.managing_memberships.application.query_memberships.MembershipRepository
import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.commands.ActivateMembershipCommand
import com.codeartify.membership.managing_memberships.domain.values.CustomerEligibility
import com.codeartify.membership.managing_memberships.domain.values.MembershipStatus
import com.codeartify.membership.managing_memberships.domain.values.PlanReferenceId
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class ActivateMembershipUseCase(
    private val commandGateway: CommandGateway,
    private val customerCacheRepository: CustomerCacheRepository,
    private val membershipRepository: MembershipRepository,
    private val fetchPlanTerms: FetchPlanTerms
) {
    fun execute(customerId: CustomerId, planReferenceId: PlanReferenceId, signedByGuardian: Boolean): MembershipId? {
        val customer = getCustomerOrThrow(customerId)
        checkNoActiveMembership(customerId)

        val planTerms = getPlanTermsOrThrow(planReferenceId)

        val activateMembershipCommand = ActivateMembershipCommand(
            MembershipId.generate(),
            customerId,
            planTerms,
            customerEligibilityFrom(customer, signedByGuardian)
        )

        return commandGateway.sendAndWait(activateMembershipCommand, MembershipId::class.java)
    }

    private fun customerEligibilityFrom(customer: CustomerEntity, signedByGuardian: Boolean): CustomerEligibility =
        CustomerEligibility(
        customer.dateOfBirth,
        signedByGuardian
    )

    private fun getPlanTermsOrThrow(planReferenceId: PlanReferenceId): PlanTerms = (fetchPlanTerms.currentTermsFor(planReferenceId)
        ?: throw IllegalArgumentException("Plan with ID ${planReferenceId.value} not found"))

    private fun getCustomerOrThrow(customerId: CustomerId): CustomerEntity {
        // customers are eventually consistent - business decision that we tolerate potential inconsistencies
        return customerCacheRepository.findById(customerId.value)
            .orElseThrow { IllegalArgumentException("Customer with ID ${customerId.value} not found") }
    }

    private fun checkNoActiveMembership(customerId: CustomerId) {
        // this is a simplification. With concurrent writes, we'd need to reserve a membership request, and release it if sth fails
        // Or we use a Process manager / saga to handle concurrency
        require(!membershipRepository.existsByCustomerIdAndStatus(customerId.value, MembershipStatus.ACTIVE.name)) {
            "Customer already has an active membership"
        }
    }
}
