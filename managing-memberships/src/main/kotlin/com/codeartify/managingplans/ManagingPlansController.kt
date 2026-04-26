package com.codeartify.managingplans

import com.codeartify.managingplans.query.PlanRepository
import com.codeartify.managingplans.query.PlanResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/plans")
class ManagingPlansController(
    private val createPlanUseCase: CreatePlanUseCase,
    private val planRepository: PlanRepository
) {

    @PostMapping
    fun create(@RequestBody request: CreatePlanRequest): ResponseEntity<String> {
        val planId = createPlanUseCase.execute(request)
        return ResponseEntity.ok(planId.value)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<PlanResponse>> {
        val plans = planRepository.findAllByOrderByDurationInMonthsAsc()
            .map { PlanResponse(it.id, it.title, it.description, it.price, it.durationInMonths) }

        return ResponseEntity.ok(plans)
    }
}
