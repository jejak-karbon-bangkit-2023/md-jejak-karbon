package com.jejakkarbon.ui.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejakkarbon.data.JejakKarbonRepository
import com.jejakkarbon.model.Guide
import kotlinx.coroutines.Dispatchers
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GuideViewModel(private val repository: JejakKarbonRepository) : ViewModel() {
    private val _guideLiveData: MutableLiveData<Result<List<Guide>>> = MutableLiveData()
    val guideLiveData: LiveData<Result<List<Guide>>> = _guideLiveData

     fun getGuide() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _guideLiveData.value = Result.Loading()
                }
                val response = repository.getGuide()
                withContext(Dispatchers.Main) {
                    _guideLiveData.value = Result.Success(response.data!!)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _guideLiveData.value = Result.Error("Prediction failed: ${e.message}")
                }
            }
        }
    }
}