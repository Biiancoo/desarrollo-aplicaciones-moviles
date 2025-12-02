package com.example.app_pasteleria_mil_sabores.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_pasteleria_mil_sabores.data.db.AppDatabase
import com.example.app_pasteleria_mil_sabores.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun loadUser(email: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val user = userDao.getUser(email)
            _uiState.value = ProfileUiState(user = user, isLoading = false)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userDao.insert(user)
            _uiState.value = _uiState.value.copy(user = user)
        }
    }
}