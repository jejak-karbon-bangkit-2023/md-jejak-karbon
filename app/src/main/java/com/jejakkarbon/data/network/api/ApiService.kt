package com.jejakkarbon.data.network.api

import com.jejakkarbon.model.DeletePlantResponse
import com.jejakkarbon.model.GetPlantsResponse
import com.jejakkarbon.model.Guide
import com.jejakkarbon.model.GuideDetail
import com.jejakkarbon.model.PredictResponse
import com.jejakkarbon.model.RegisterRequest
import com.jejakkarbon.model.RegisterResponse
import com.jejakkarbon.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @Multipart
    @POST("/predict")
    suspend fun predict(
        @Part file: MultipartBody.Part,
        @Part("distance") distance: Int,
        @Part("transport") transport: String
    ): Response<PredictResponse>

    @Multipart
    @POST("/predict")
    suspend fun predict(
        @Part file: MultipartBody.Part,
    ): Response<PredictResponse>

    @Multipart
    @POST("/predict")
    suspend fun predict(
        @Part("distance") distance: RequestBody, @Part("transport") transport: RequestBody
    ): Response<PredictResponse>

    @GET("/user/{user_id}")
    suspend fun getUserData(
        @Path("user_id") userId: String
    ): Response<User>

    @GET("/articles")
    suspend fun getGuide(): Response<List<Guide>>

    @DELETE("/user/{user_id}/plant/{plant_index}")
    suspend fun deletePlant(
        @Path("user_id") userId: String, @Path("plant_index") plantIndex: Int
    ): Response<DeletePlantResponse>

    @GET("/user/{user_id}/plants")
    suspend fun getPlants(@Path("user_id") userId: String): Response<GetPlantsResponse>

    @GET("articles/{articleId}")
    suspend fun getGuideDetail(@Path("articleId") articleId: String): Response<GuideDetail>
}