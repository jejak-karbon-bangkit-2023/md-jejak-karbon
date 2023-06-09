package com.jejakkarbon.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejakkarbon.data.JejakKarbonRepository
import com.jejakkarbon.model.RegisterRequest
import com.jejakkarbon.model.RegisterResponse
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerRepository: JejakKarbonRepository) : ViewModel() {
    private val _registerState = MutableLiveData<Result<RegisterResponse>>()
    val registerState: LiveData<Result<RegisterResponse>> = _registerState

    private val _registerWithGoogleState = MutableLiveData<Result<Unit>>()
    val registerWithGoogleState = _registerWithGoogleState

    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = Result.Loading()
            val result = registerRepository.register(request)
            _registerState.value = result
        }
    }

    fun registerWithGoogle(idToken: String) {
        viewModelScope.launch {
            _registerWithGoogleState.value = Result.Loading()
            val result = registerRepository.loginWithGoogle(idToken)
            _registerWithGoogleState.value = result
        }
    }
}