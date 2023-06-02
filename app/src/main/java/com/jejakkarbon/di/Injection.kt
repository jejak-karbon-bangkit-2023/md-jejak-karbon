package com.jejakkarbon.di

import androidx.appcompat.app.AppCompatActivity
import com.jejakkarbon.data.AuthenticationRepository
import com.jejakkarbon.data.FirebaseAuthenticationRepository
import com.jejakkarbon.ui.login.LoginViewModel
import com.jejakkarbon.ui.register.RegisterViewModel

object Injection {
    private val authenticationRepository: AuthenticationRepository by lazy {
        FirebaseAuthenticationRepository()
    }

    fun provideLoginViewModel(activity: AppCompatActivity): LoginViewModel {
        return LoginViewModel(authenticationRepository)
    }

    fun provideRegisterViewModel(activity: AppCompatActivity): RegisterViewModel {
        return RegisterViewModel(authenticationRepository)
    }
}