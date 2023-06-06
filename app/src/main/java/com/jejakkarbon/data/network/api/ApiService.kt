package com.jejakkarbon.data.network.api

import android.service.autofill.UserData
import com.jejakkarbon.model.DeletePlantResponse
import com.jejakkarbon.model.GetPlantsResponse
import com.jejakkarbon.model.PredictResponse
import com.jejakkarbon.model.RegisterRequest
import com.jejakkarbon.model.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("/register")
    suspend fun register(@Body register: RegisterRequest): Response<RegisterResponse>

    @Multipart
    @POST("/predict")
    suspend fun predict(@Part file: MultipartBody.Part): Response<PredictResponse>

    @GET("/user/{user_id}")
    suspend fun getUserData(@Path("user_id") userId: String): Response<UserData>

    @DELETE("/user/{user_id}/plant/{plant_index}")
    suspend fun deletePlant(@Path("user_id") userId: String, @Path("plant_index") plantIndex: Int): Response<DeletePlantResponse>

    @GET("/user/{user_id}/plants")
    suspend fun getPlants(@Path("user_id") userId: String): Response<GetPlantsResponse>
}