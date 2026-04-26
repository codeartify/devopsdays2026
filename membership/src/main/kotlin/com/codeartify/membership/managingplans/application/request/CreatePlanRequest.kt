package com.codeartify.membership.managingplans.application.request

data class CreatePlanRequest(
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
