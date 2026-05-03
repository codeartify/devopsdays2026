package com.codeartify.membership.managing_memberships.use_case.activate_membership

import com.codeartify.membership.customer_cache.CustomerCacheRepository
import com.codeartify.membership.managing_memberships.domain.CustomerId
import com.codeartify.membership.managing_memberships.domain.MembershipId
import com.codeartify.membership.managing_memberships.domain.commands.ActivateMembershipCommand
import com.codeartify.membership.managing_memberships.domain.values.PlanReferenceId
import com.codeartify.membership.managing_memberships.use_case.query_memberships.MembershipRepository
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class ActivateMembershipUseCase(
    private val commandGateway: CommandGateway,
    private val customerCacheRepository: CustomerCacheRepository,
    private val membershipRepository: MembershipRepository,
    private val fetchPlanTerms: FetchPlanTerms
) {
    /*
        ## 1 Send a command
        * send a command to activate a membership
          * the command should contain the following information:
            * the customer ID (value object, not null)
            * the agreed upon plan terms containing
              * the plan ID
              * plan duration in months
              * plan price
            * the customer eligibility
              * a customers date of birth
              * if the customer was signed by a guardian if they were underage
          * ensure the customer exists (use the local customer cache)
          * ensure the customer does not already have an active membership (use the local membership projection (MembershipRepository)
          * get the current plan terms for the plan ID
            * the plan repository is owned by another bounded context in the same application: don't directly access it (could be an API call in the future)
            * instead, implement an adapter (see PlanTermsProvider) that uses that repository and create a value object only for the plan terms needed in the membership
          * use Axon's commandGateway.sendAndWait(...) to send the command to the Membership aggregate
     */
    fun execute(customerId: CustomerId, planReferenceId: PlanReferenceId, signedByGuardian: Boolean): MembershipId? {
        // TODO: implement checks and command creation
        // Use the provided repositories to fetch data and check customer eligibility
        val activateMembershipCommand = ActivateMembershipCommand(MembershipId.generate(), customerId)

        return commandGateway.sendAndWait(activateMembershipCommand, MembershipId::class.java)
    }

}
