package com.jejakkarbon.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejakkarbon.data.JejakKarbonRepository
import com.jejakkarbon.model.Guide
import com.jejakkarbon.model.User
import kotlinx.coroutines.Dispatchers
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel(private val repository: JejakKarbonRepository) : ViewModel() {
    private val _dashboardLiveData: MutableLiveData<Result<List<Guide>>> = MutableLiveData()
    val dashboardLiveData: LiveData<Result<List<Guide>>> = _dashboardLiveData
    private val _userLiveData: MutableLiveData<Result<User>> = MutableLiveData()
    val userLiveData: LiveData<Result<User>> = _userLiveData

     fun getGuide() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _dashboardLiveData.value = Result.Loading()
                }
                val response = repository.getGuide()
                withContext(Dispatchers.Main) {
                    _dashboardLiveData.value = Result.Success(response.data!!)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _dashboardLiveData.value = Result.Error("Prediction failed: ${e.message}")
                }
            }
        }
    }

     fun getUserDetail(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _userLiveData.value = Result.Loading()
                }
                val response = repository.getUserData(userId)
                withContext(Dispatchers.Main) {
                    _userLiveData.value = response
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _userLiveData.value = Result.Error("get user data failed: ${e.message}")
                }
            }
        }
    }
}