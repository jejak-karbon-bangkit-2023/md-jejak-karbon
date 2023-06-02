package com.jejakkarbon.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejakkarbon.data.AuthenticationRepository
import com.jejakkarbon.utils.ResultState
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthenticationRepository) : ViewModel() {
    private val _loginState = MutableLiveData<ResultState<Unit>>()
    val loginState = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = ResultState.Loading()

            val result = authRepository.login(email, password)
            _loginState.value = result
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginState.value = ResultState.Loading()

            val result = authRepository.loginWithGoogle(idToken)
            _loginState.value = result
        }
    }
}