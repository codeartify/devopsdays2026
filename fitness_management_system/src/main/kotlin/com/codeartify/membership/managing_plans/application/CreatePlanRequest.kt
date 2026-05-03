package com.codeartify.membership.managing_plans.application

data class CreatePlanRequest(
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
