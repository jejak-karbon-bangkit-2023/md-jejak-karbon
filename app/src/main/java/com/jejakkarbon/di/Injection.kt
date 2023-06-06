package com.jejakkarbon.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.jejakkarbon.data.JejakKarbonRepository
import com.jejakkarbon.data.JejakKarbonImplRepository
import com.jejakkarbon.data.network.api.ApiConfig
import com.jejakkarbon.ui.login.LoginViewModel
import com.jejakkarbon.ui.register.RegisterViewModel

object Injection {
    private lateinit var jejakKarbonRepository: JejakKarbonRepository

    fun initialize(context: Context) {
        ApiConfig.initialize(context)
        val apiService = ApiConfig.getApiService()
        jejakKarbonRepository = JejakKarbonImplRepository(apiService)
    }

    fun provideLoginViewModel(activity: AppCompatActivity): LoginViewModel {
        return LoginViewModel(jejakKarbonRepository)
    }

    fun provideRegisterViewModel(activity: AppCompatActivity): RegisterViewModel {
        return RegisterViewModel(jejakKarbonRepository)
    }
}