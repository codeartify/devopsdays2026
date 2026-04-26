package com.codeartify.membership.contexts.managingplans.application.request

data class UpdatePlanRequest(
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
