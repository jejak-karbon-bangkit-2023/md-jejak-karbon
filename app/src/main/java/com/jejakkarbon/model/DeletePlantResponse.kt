package com.jejakkarbon.model

data class DeletePlantResponse(
    val message: String,
    val error: Boolean,
    val data: Plant
)