package com.jejakkarbon.model

data class PredictResponse(
    val data_plant: PredictDataPlant?,
    val data_transport: PredictDataTransport?,
    val message: String,
    val error: Boolean
)

data class PredictDataPlant(
    val uuid: String,
    val user_id: String,
    val email: String,
    val name: String,
    val plant: List<Plant>
)

data class PredictDataTransport(
    val uuid: String,
    val user_id: String,
    val email: String,
    val name: String,
    val transport: List<Transport>
)






