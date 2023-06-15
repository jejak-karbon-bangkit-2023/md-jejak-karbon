package com.jejakkarbon.ui.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejakkarbon.data.JejakKarbonRepository
import com.jejakkarbon.model.GuideDetail
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GuideDetailViewModel(private val repository: JejakKarbonRepository) : ViewModel() {
    private val _guideDetailLiveData: MutableLiveData<Result<GuideDetail>> = MutableLiveData()
    val guideDetailLiveData: LiveData<Result<GuideDetail>> = _guideDetailLiveData

    suspend fun getDetailGuide(guideId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getGuideDetail(guideId)
                withContext(Dispatchers.Main) {
                    _guideDetailLiveData.value = result
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _guideDetailLiveData.value =
                        Result.Error(e.message ?: "Failed to fetch guide details.")
                }
            }


        }
    }
}