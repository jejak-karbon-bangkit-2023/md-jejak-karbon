package com.jejakkarbon.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val display_name: String,
)

