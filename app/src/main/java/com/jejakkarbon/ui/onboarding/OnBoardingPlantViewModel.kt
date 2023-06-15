package com.jejakkarbon.ui.onboarding

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

class OnBoardingPlantViewModel(private val repository: JejakKarbonRepository) : ViewModel() {
    private val _onBoardingPlantResult = MutableLiveData<Result<PredictResponse?>>()


    fun predict(file: MultipartBody.Part?, distance: Int?, transport: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _onBoardingPlantResult.value = Result.Loading()
                }
                val response = repository.predict(file, distance, transport)
                withContext(Dispatchers.Main) {
                    _onBoardingPlantResult.value = Result.Success(response.data)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _onBoardingPlantResult.value = Result.Error("Prediction failed: ${e.message}")
                }
            }
        }
    }
}