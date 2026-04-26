package com.codeartify.managingplans

data class CreatePlanRequest(
    val title: String,
    val description: String,
    val price: Int,
    val durationInMonths: Int
)
