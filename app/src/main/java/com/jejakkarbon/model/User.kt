package com.jejakkarbon.model

data class User(
    val message: String,
    val error: Boolean,
    val data: UserDataDetails
)

data class UserDataDetails(
    val user_id: String,
    val email: String,
    val name: String,
    val plant: List<Plant>,
    val transport: List<Transport>,
    val c_score: String,
    val status: String
)

