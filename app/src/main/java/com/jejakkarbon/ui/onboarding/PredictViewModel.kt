package com.jejakkarbon.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejakkarbon.data.JejakKarbonRepository
import com.jejakkarbon.model.PredictResponse
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody


class PredictViewModel(private val repository: JejakKarbonRepository) : ViewModel() {
    private val _predictResult = MutableLiveData<Result<PredictResponse?>>()
    val predictResult: LiveData<Result<PredictResponse?>> = _predictResult

    fun predict(file: MultipartBody.Part) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _predictResult.value = Result.Loading()
                }
                val response = repository.predict(file)
                withContext(Dispatchers.Main) {
                    _predictResult.value = Result.Success(response.data)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _predictResult.value = Result.Error("Prediction failed: ${e.message}")
                }
            }
        }
    }
}