package com.jejakkarbon.data


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jejakkarbon.data.network.api.ApiService
import com.jejakkarbon.model.DeletePlantResponse
import com.jejakkarbon.model.GetPlantsResponse
import com.jejakkarbon.model.Guide
import com.jejakkarbon.model.PredictResponse
import com.jejakkarbon.model.RegisterRequest
import com.jejakkarbon.model.RegisterResponse
import com.jejakkarbon.model.User
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.tasks.await
import okhttp3.MultipartBody


class JejakKarbonImplRepository(private val apiService: ApiService) : JejakKarbonRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun register(
        request: RegisterRequest
    ): Result<RegisterResponse> {
        return try {
            val response = apiService.register(
                RegisterRequest(
                    request.email, request.password, request.display_name
                )
            )
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Registration failed: ${response.body()?.message.toString()}")
            }
        } catch (e: Exception) {
            Result.Error("Registration failed: ${e.message.toString()}")
        }
    }


    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Google login failed: ${e.message}")
        }
    }

    override suspend fun predict(file: MultipartBody.Part): Result<PredictResponse> {
        return try {
            val response = apiService.predict(file)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Prediction failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Prediction failed: ${e.message}")
        }
    }

    override suspend fun getGuide(): Result<List<Guide>> {
        return try {
            val response = apiService.getGuide()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("get Articles Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("get Articles failed: ${e.message}")
        }
    }


    override suspend fun getUserData(userId: String): Result<User> {
        return try {
            val response = apiService.getUserData(userId)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to retrieve user data: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Failed to retrieve user data: ${e.message}")
        }
    }

    override suspend fun deletePlant(userId: String, plantIndex: Int): Result<DeletePlantResponse> {
        return try {
            val response = apiService.deletePlant(userId, plantIndex)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to delete plant: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Failed to delete plant: ${e.message}")
        }
    }

    override suspend fun getPlants(userId: String): Result<GetPlantsResponse> {
        return try {
            val response = apiService.getPlants(userId)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to retrieve plants: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Failed to retrieve plants: ${e.message}")
        }
    }
}


