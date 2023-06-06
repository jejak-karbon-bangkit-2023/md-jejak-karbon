package com.jejakkarbon.model

data class GetPlantsResponse(
    val message: String,
    val error: Boolean,
    val data: List<Plant>
)