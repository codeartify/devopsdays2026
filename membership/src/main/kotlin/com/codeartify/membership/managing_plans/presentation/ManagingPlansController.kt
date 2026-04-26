package com.codeartify.membership.managing_plans.presentation

import com.codeartify.membership.managing_plans.application.request.CreatePlanRequest
import com.codeartify.membership.managing_plans.application.request.UpdatePlanRequest
import com.codeartify.membership.managing_plans.application.use_case.DefinePlanUseCase
import com.codeartify.membership.managing_plans.application.use_case.DeletePlanUseCase
import com.codeartify.membership.managing_plans.application.use_case.UpdatePlanUseCase
import com.codeartify.membership.managing_plans.data_access.PlanRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
