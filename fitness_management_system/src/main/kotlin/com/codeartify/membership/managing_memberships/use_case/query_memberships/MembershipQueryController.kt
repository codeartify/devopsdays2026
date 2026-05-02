package com.codeartify.membership.managing_memberships.use_case.query_memberships

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/memberships")
class MembershipQueryController(
    private val membershipRepository: MembershipRepository
) {

    @GetMapping
    fun getAll(): ResponseEntity<List<MembershipResponse>> =
        ResponseEntity.ok(
            membershipRepository.findAll()
                .map { it.toResponse() }
        )

    @GetMapping("/{membershipId}")
    fun getById(@PathVariable membershipId: String): ResponseEntity<MembershipResponse> =
        membershipRepository.findById(membershipId)
            .map { ResponseEntity.ok(it.toResponse()) }
            .orElseGet { ResponseEntity.notFound().build() }

    private fun MembershipEntity.toResponse(): MembershipResponse =
        MembershipResponse(
            id = id,
            customerId = customerId,
            planId = planId,
            planDuration = planDuration,
            planPrice = planPrice,
            customerDateOfBirth = customerDateOfBirth,
            guardianSignaturePresent = guardianSignaturePresent,
            status = status,
            pauseStartDate = pauseStartDate,
            pauseEndDate = pauseEndDate,
            pauseDurationDays = pauseDurationDays
        )
}
