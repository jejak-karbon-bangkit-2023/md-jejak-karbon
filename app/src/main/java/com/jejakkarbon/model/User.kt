package com.jejakkarbon.model

data class User(
    val user_id: String,
    val email: String,
    val name: String,
    val plant: List<Plant>
)

