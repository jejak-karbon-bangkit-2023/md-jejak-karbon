package com.jejakkarbon.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejakkarbon.data.JejakKarbonRepository
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repository: JejakKarbonRepository) : ViewModel() {
    private val _editProfileState = MutableLiveData<Result<Unit>>()
    val editProfileState: LiveData<Result<Unit>> = _editProfileState

    fun editProfile(userId: String, newName: String, newEmail: String) {
        viewModelScope.launch {
            _editProfileState.value = Result.Loading()

            val result = repository.editProfile(userId, newName, newEmail)
            _editProfileState.value = result
        }
    }
}