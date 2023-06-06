package com.jejakkarbon.data

import com.jejakkarbon.model.RegisterRequest
import com.jejakkarbon.utils.Result
import okhttp3.MultipartBody

interface JejakKarbonRepository {
    suspend fun register(registerRequest: RegisterRequest): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun loginWithGoogle(idToken: String): Result<Unit>

    suspend fun predict(file: MultipartBody.Part): Result<Unit>
    suspend fun getUserData(userId: String): Result<Unit>
    suspend fun deletePlant(userId: String, plantIndex: Int): Result<Unit>
    suspend fun getPlants(userId: String): Result<Unit>
}
