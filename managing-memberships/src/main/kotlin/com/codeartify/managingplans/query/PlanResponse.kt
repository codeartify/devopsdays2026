package com.codeartify.managingplans.query

data class PlanResponse(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
