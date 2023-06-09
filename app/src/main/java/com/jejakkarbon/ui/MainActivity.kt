package com.jejakkarbon.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
    }

    private fun checkUserLoginStatus() {
        val userToken by lazy { preferences.getToken() }
        Log.d("Main Token", userToken.token.toString())
        Log.d("Main Login", userToken.isLogin.toString())
        Log.d("Main First Login", userToken.isFirstLogin.toString())
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