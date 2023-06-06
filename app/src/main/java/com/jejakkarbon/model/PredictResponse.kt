package com.jejakkarbon.model

data class PredictResponse(
    val data: PredictData,
    val message: String,
    val error: Boolean
)

data class PredictData(
    val uuid: String,
    val user_id: String,
    val email: String,
    val name: String,
    val plant: List<Plant>
)






