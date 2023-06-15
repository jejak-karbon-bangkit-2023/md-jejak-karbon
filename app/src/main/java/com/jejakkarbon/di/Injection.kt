package com.jejakkarbon.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.jejakkarbon.data.JejakKarbonImplRepository
import com.jejakkarbon.data.JejakKarbonRepository
import com.jejakkarbon.data.network.api.ApiConfig
import com.jejakkarbon.ui.dashboard.DashboardViewModel
import com.jejakkarbon.ui.guide.GuideDetailViewModel
import com.jejakkarbon.ui.guide.GuideViewModel
import com.jejakkarbon.ui.login.LoginViewModel
import com.jejakkarbon.ui.onboarding.OnBoardingPlantViewModel
import com.jejakkarbon.ui.onboarding.PredictViewModel
import com.jejakkarbon.ui.profile.EditProfileViewModel
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

    fun providePredictViewModel(activity: AppCompatActivity): PredictViewModel{
        return PredictViewModel(jejakKarbonRepository)
    }
    fun provideDashboardViewModel(activity: AppCompatActivity): DashboardViewModel{
        return DashboardViewModel(jejakKarbonRepository)
    }

    fun provideEditProfileViewModel(activity: AppCompatActivity): EditProfileViewModel{
        return EditProfileViewModel(jejakKarbonRepository)
    }

    fun provideguideViewModel(activity: AppCompatActivity): GuideViewModel{
        return GuideViewModel(jejakKarbonRepository)
    }
    fun provideguideDetailViewModel(activity: AppCompatActivity): GuideDetailViewModel{
       return GuideDetailViewModel(jejakKarbonRepository)
    }

    fun provideOnBoardingPlantViewModel(activity: AppCompatActivity): OnBoardingPlantViewModel{
        return OnBoardingPlantViewModel(jejakKarbonRepository)
    }
}