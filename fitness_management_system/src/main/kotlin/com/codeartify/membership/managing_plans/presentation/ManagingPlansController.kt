package com.codeartify.membership.managing_plans.presentation

import com.codeartify.membership.managing_plans.application.CreatePlanRequest
import com.codeartify.membership.managing_plans.application.DefinePlanUseCase
import com.codeartify.membership.managing_plans.application.DeletePlanUseCase
import com.codeartify.membership.managing_plans.application.UpdatePlanUseCase
import com.codeartify.membership.managing_plans.application.UpdatePlanRequest
import com.codeartify.membership.managing_plans.data_access.PlanRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/plans")
class ManagingPlansController(
    private val definePlanUseCase: DefinePlanUseCase,
    private val updatePlanUseCase: UpdatePlanUseCase,
    private val deletePlanUseCase: DeletePlanUseCase,
    private val planRepository: PlanRepository
) {

    @PostMapping
    fun create(@RequestBody request: CreatePlanRequest): ResponseEntity<String> {
        val planId = definePlanUseCase.execute(request)
        return ResponseEntity.ok(planId.value)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<PlanResponse>> {
        val plans = planRepository.findAllByOrderByDurationAsc()
            .map {
                PlanResponse(
                    it.planId.value,
                    it.title.value,
                    it.description.value,
                    it.price.value,
                    it.duration.value
                )
            }

        return ResponseEntity.ok(plans)
    }

    @PutMapping("/{planId}")
    fun update(
        @PathVariable planId: String,
        @RequestBody request: UpdatePlanRequest
    ): ResponseEntity<Void> {
        updatePlanUseCase.execute(planId, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{planId}")
    fun delete(@PathVariable planId: String): ResponseEntity<Void> {
        deletePlanUseCase.execute(planId)
        return ResponseEntity.noContent().build()
    }
}
