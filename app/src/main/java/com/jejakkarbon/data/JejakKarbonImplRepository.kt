package com.jejakkarbon.data


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jejakkarbon.data.network.api.ApiService
import com.jejakkarbon.model.RegisterRequest
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.tasks.await
import okhttp3.MultipartBody


class JejakKarbonImplRepository(private val apiService: ApiService) :
    JejakKarbonRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            val registrationData = RegisterRequest(registerRequest.name, registerRequest.email, registerRequest.password)
            val response = apiService.register(registrationData)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Registration failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Registration failed: ${e.message}")
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

    override suspend fun predict(file: MultipartBody.Part): Result<Unit> {
        return try {
            val response = apiService.predict(file)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Prediction failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Prediction failed: ${e.message}")
        }
    }

    override suspend fun getUserData(userId: String): Result<Unit> {
        return try {
            val response = apiService.getUserData(userId)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Failed to retrieve user data: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Failed to retrieve user data: ${e.message}")
        }
    }

    override suspend fun deletePlant(userId: String, plantIndex: Int): Result<Unit> {
        return try {
            val response = apiService.deletePlant(userId, plantIndex)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Failed to delete plant: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Failed to delete plant: ${e.message}")
        }
    }

    override suspend fun getPlants(userId: String): Result<Unit> {
        return try {
            val response = apiService.getPlants(userId)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Failed to retrieve plants: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Failed to retrieve plants: ${e.message}")
        }
    }
}

