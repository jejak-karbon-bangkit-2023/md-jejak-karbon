package com.jejakkarbon.data

import com.jejakkarbon.model.User
import com.jejakkarbon.utils.ResultState

interface AuthenticationRepository {
    suspend fun register(user: User): ResultState<Unit>
    suspend fun login(email: String, password: String): ResultState<Unit>
    suspend fun loginWithGoogle(idToken: String): ResultState<Unit>
    suspend fun registerWithGoogle(idToken: String): ResultState<Unit>
}
