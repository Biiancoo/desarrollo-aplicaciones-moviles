package com.example.app_pasteleria_mil_sabores.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_pasteleria_mil_sabores.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val context: Context = application.applicationContext

    private val userRepository = UserRepository(application)

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Email y contraseña obligatorios")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            userRepository.login(email, password)
                .onSuccess { user ->
                    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putString("current_user_email", user.email).apply()
                    _uiState.value = _uiState.value.copy(success = true, isLoading = false)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(error = it.message, isLoading = false)
                }
        }
    }
}