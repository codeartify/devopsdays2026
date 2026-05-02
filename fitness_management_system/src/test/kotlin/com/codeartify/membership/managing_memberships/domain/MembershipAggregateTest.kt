package com.codeartify.membership.managing_memberships.domain

import com.codeartify.membership.managing_memberships.domain.commands.ActivateMembershipCommand
import com.codeartify.membership.managing_memberships.domain.commands.CancelMembershipCommand
import com.codeartify.membership.managing_memberships.domain.commands.PauseMembershipCommand
import com.codeartify.membership.managing_memberships.domain.commands.ReactivateMembershipCommand
import com.codeartify.membership.managing_memberships.domain.commands.ResumeMembershipCommand
import com.codeartify.membership.managing_memberships.domain.commands.SuspendMembershipCommand
import com.codeartify.membership.managing_memberships.domain.events.MembershipActivatedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipCancelledEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipPausedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipReactivatedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipResumedEvent
import com.codeartify.membership.managing_memberships.domain.events.MembershipSuspendedEvent
import com.codeartify.membership.managing_memberships.domain.values.CustomerEligibility
import com.codeartify.membership.managing_memberships.domain.values.Duration
import com.codeartify.membership.managing_memberships.domain.values.PausePeriod
import com.codeartify.membership.managing_memberships.domain.values.PlanReferenceId
import com.codeartify.membership.managing_memberships.domain.values.PlanTerms
import com.codeartify.membership.managing_memberships.domain.values.Price
import org.axonframework.eventsourcing.configuration.EventSourcedEntityModule
import org.axonframework.eventsourcing.configuration.EventSourcingConfigurer
import org.axonframework.test.fixture.AxonTestFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.util.stream.Stream

class MembershipAggregateTest {

    private lateinit var fixture: AxonTestFixture

    private val membershipId = MembershipId.of("b84333b2-5ed9-4488-a0d7-edee5110bc20")
    private val customerId = CustomerId.of("customer-1")
    private val planTerms = PlanTerms(
        planReferenceId = PlanReferenceId.of("b82a8402-0a42-463a-ad46-096804c25e53"),
        duration = Duration.of(12),
        price = Price.of(599)
    )
    private val customerEligibility = CustomerEligibility(
        dateOfBirth = LocalDate.of(1990, 1, 1),
        guardianSignaturePresent = false
    )
    private val pauseStartDate = LocalDate.now().plusDays(1)
    private val pausePeriod = PausePeriod(
        startDate = pauseStartDate,
        endDate = pauseStartDate.plusDays(30)
    )

    @BeforeEach
    fun setUp() {
        fixture = AxonTestFixture.with(
            EventSourcingConfigurer.create()
                .registerEntity(EventSourcedEntityModule.autodetected(MembershipId::class.java, Membership::class.java))
        )
    }

    @AfterEach
    fun tearDown() {
        fixture.stop()
    }

    @Test
    fun `activating membership emits activated event and returns membership id`() {
        val command = ActivateMembershipCommand(membershipId, customerId, planTerms, customerEligibility)

        fixture.given()
            .noPriorActivity()
            .`when`()
            .command(command)
            .then()
            .success()
            .resultMessagePayload(membershipId)
            .events(MembershipActivatedEvent(membershipId, customerId, planTerms, customerEligibility))
    }

    @Test
    fun `active membership can be paused`() {
        fixture.givenActiveMembership()
            .`when`()
            .command(PauseMembershipCommand(membershipId, pausePeriod))
            .then()
            .success()
            .events(MembershipPausedEvent(membershipId, pausePeriod))
    }

    @Test
    fun `paused membership can be resumed`() {
        fixture.givenPausedMembership()
            .`when`()
            .command(ResumeMembershipCommand(membershipId))
            .then()
            .success()
            .events(MembershipResumedEvent(membershipId))
    }

    @Test
    fun `active membership can be suspended`() {
        fixture.givenActiveMembership()
            .`when`()
            .command(SuspendMembershipCommand(membershipId))
            .then()
            .success()
            .events(MembershipSuspendedEvent(membershipId))
    }

    @Test
    fun `suspended membership can be reactivated`() {
        fixture.givenSuspendedMembership()
            .`when`()
            .command(ReactivateMembershipCommand(membershipId))
            .then()
            .success()
            .events(MembershipReactivatedEvent(membershipId))
    }

    @Test
    fun `active membership can be cancelled`() {
        fixture.givenActiveMembership()
            .`when`()
            .command(CancelMembershipCommand(membershipId))
            .then()
            .success()
            .events(MembershipCancelledEvent(membershipId))
    }

    @Test
    fun `paused membership can be cancelled`() {
        fixture.givenPausedMembership()
            .`when`()
            .command(CancelMembershipCommand(membershipId))
            .then()
            .success()
            .events(MembershipCancelledEvent(membershipId))
    }

    @Test
    fun `suspended membership can be cancelled`() {
        fixture.givenSuspendedMembership()
            .`when`()
            .command(CancelMembershipCommand(membershipId))
            .then()
            .success()
            .events(MembershipCancelledEvent(membershipId))
    }

    @Test
    fun `active membership cannot be resumed`() {
        fixture.givenActiveMembership()
            .`when`()
            .command(ResumeMembershipCommand(membershipId))
            .then()
            .exception(IllegalArgumentException::class.java, "Only paused memberships can be resumed")
    }

    @Test
    fun `paused membership cannot be paused again`() {
        fixture.givenPausedMembership()
            .`when`()
            .command(PauseMembershipCommand(membershipId, pausePeriod))
            .then()
            .exception(IllegalArgumentException::class.java, "Membership is already paused")
    }

    @ParameterizedTest
    @MethodSource("commandsRejectedForCancelledMembership")
    fun `cancelled membership rejects every state transition command`(command: Any) {
        fixture.givenCancelledMembership()
            .`when`()
            .command(command)
            .then()
            .exception(IllegalArgumentException::class.java, "Cancelled memberships are terminal")
    }

    private fun AxonTestFixture.givenActiveMembership() =
        given().events(MembershipActivatedEvent(membershipId, customerId, planTerms, customerEligibility))

    private fun AxonTestFixture.givenPausedMembership() =
        given().events(
            MembershipActivatedEvent(membershipId, customerId, planTerms, customerEligibility),
            MembershipPausedEvent(membershipId, pausePeriod)
        )

    private fun AxonTestFixture.givenSuspendedMembership() =
        given().events(
            MembershipActivatedEvent(membershipId, customerId, planTerms, customerEligibility),
            MembershipSuspendedEvent(membershipId)
        )

    private fun AxonTestFixture.givenCancelledMembership() =
        given().events(
            MembershipActivatedEvent(membershipId, customerId, planTerms, customerEligibility),
            MembershipCancelledEvent(membershipId)
        )

    companion object {
        private val cancelledMembershipId = MembershipId.of("b84333b2-5ed9-4488-a0d7-edee5110bc20")
        private val cancelledPauseStartDate = LocalDate.now().plusDays(1)
        private val cancelledPausePeriod = PausePeriod(
            startDate = cancelledPauseStartDate,
            endDate = cancelledPauseStartDate.plusDays(30)
        )

        @JvmStatic
        fun commandsRejectedForCancelledMembership(): Stream<Any> =
            Stream.of(
                PauseMembershipCommand(cancelledMembershipId, cancelledPausePeriod),
                ResumeMembershipCommand(cancelledMembershipId),
                SuspendMembershipCommand(cancelledMembershipId),
                ReactivateMembershipCommand(cancelledMembershipId),
                CancelMembershipCommand(cancelledMembershipId)
            )
    }
}
