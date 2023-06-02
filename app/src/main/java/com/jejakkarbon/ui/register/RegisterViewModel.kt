package com.jejakkarbon.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejakkarbon.data.AuthenticationRepository
import com.jejakkarbon.model.User
import com.jejakkarbon.utils.ResultState
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthenticationRepository) : ViewModel() {
    private val _registerState = MutableLiveData<ResultState<Unit>>()
    val registerState = _registerState

    fun register(user: User) {
        viewModelScope.launch {
            _registerState.value = ResultState.Loading()

            val result = authRepository.register(user)
            _registerState.value = result
        }
    }

    fun registerWithGoogle(idToken: String) {
        viewModelScope.launch {
            _registerState.value = ResultState.Loading()

            val result = authRepository.loginWithGoogle(idToken)
            _registerState.value = result
        }
    }
}