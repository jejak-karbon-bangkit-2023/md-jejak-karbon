package com.jejakkarbon.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejakkarbon.data.JejakKarbonRepository
import com.jejakkarbon.model.RegisterRequest
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: JejakKarbonRepository) : ViewModel() {
    private val _registerState = MutableLiveData<Result<Unit>>()
    val registerState = _registerState

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = Result.Loading()

            val result = authRepository.register(registerRequest)
            _registerState.value = result
        }
    }

    fun registerWithGoogle(idToken: String) {
        viewModelScope.launch {
            _registerState.value = Result.Loading()
            val result = authRepository.loginWithGoogle(idToken)
            _registerState.value = result
        }
    }
}