package com.jejakkarbon.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejakkarbon.data.JejakKarbonRepository
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: JejakKarbonRepository) : ViewModel() {
    private val _loginState = MutableLiveData<Result<Unit>>()
    val loginState = _loginState

    val userToken = MutableLiveData<String>()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Result.Loading()

            val result = authRepository.login(email, password)
            if (result is Result.Success) {
                userToken.value = result.data.toString() // Set the user token
            }
            _loginState.value = result
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginState.value = Result.Loading()

            val result = authRepository.loginWithGoogle(idToken)
            _loginState.value = result
        }
    }
}