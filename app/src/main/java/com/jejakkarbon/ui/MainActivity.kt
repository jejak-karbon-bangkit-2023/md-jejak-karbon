package com.jejakkarbon.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.jejakkarbon.databinding.ActivityMainBinding
import com.jejakkarbon.preferences.Preferences
import com.jejakkarbon.ui.dashboard.DashboardActivity
import com.jejakkarbon.ui.login.LoginActivity
import com.jejakkarbon.ui.onboarding.OnboardingActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = Preferences(this)
        checkUserLoginStatus()
        refreshUserToken()
    }

    private fun checkUserLoginStatus() {
        val userToken by lazy { preferences.getToken() }
        if (userToken.isLogin) {
            if (userToken.isFirstLogin) {
                navigateToOnboardingActivity()
            } else {
                navigateToDashboardActivity()
            }
        } else {
            navigateToLoginActivity()
        }
    }


    private fun refreshUserToken() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val tokenResult = task.result
                val refreshedToken = tokenResult?.token
                val expirationTimestamp = tokenResult?.expirationTimestamp

                if (refreshedToken != null && expirationTimestamp != null) {
                    val userToken = preferences.getToken()
                    userToken.token = refreshedToken
                    preferences.setToken(userToken)
                }
            }
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }
}