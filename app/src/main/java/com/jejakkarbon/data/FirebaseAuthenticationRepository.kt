package com.jejakkarbon.data


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jejakkarbon.model.User
import com.jejakkarbon.utils.ResultState
import kotlinx.coroutines.tasks.await


class FirebaseAuthenticationRepository : AuthenticationRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    override suspend fun register(user: User): ResultState<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(user.email, user.password).await()
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error("Registration failed: ${e.message}")
        }
    }

    override suspend fun login(email: String, password: String): ResultState<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error("Login failed: ${e.message}")
        }
    }

    override suspend fun loginWithGoogle(idToken: String): ResultState<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error("Google login failed: ${e.message}")
        }
    }

    override suspend fun registerWithGoogle(idToken: String): ResultState<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error("Google register failed: ${e.message}")
        }
    }
}

