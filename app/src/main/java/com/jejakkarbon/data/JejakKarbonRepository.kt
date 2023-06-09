package com.jejakkarbon.data

import com.jejakkarbon.model.DeletePlantResponse
import com.jejakkarbon.model.GetPlantsResponse
import com.jejakkarbon.model.Guide
import com.jejakkarbon.model.PredictResponse
import com.jejakkarbon.model.RegisterRequest
import com.jejakkarbon.model.RegisterResponse
import com.jejakkarbon.model.User
import com.jejakkarbon.utils.Result
import okhttp3.MultipartBody

interface JejakKarbonRepository {
    suspend fun register(request: RegisterRequest): Result<RegisterResponse>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun loginWithGoogle(idToken: String): Result<Unit>

    suspend fun predict(file: MultipartBody.Part): Result<PredictResponse>
    suspend fun getUserData(userId: String): Result<User>
    suspend fun getGuide(): Result<List<Guide>>
    suspend fun deletePlant(userId: String, plantIndex: Int): Result<DeletePlantResponse>
    suspend fun getPlants(userId: String): Result<GetPlantsResponse>
}
