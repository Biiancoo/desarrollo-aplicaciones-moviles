package com.example.app_pasteleria_mil_sabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_pasteleria_mil_sabores.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.fold

data class AuthState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userId: Int = 0,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)

            val result = userRepository.login(email, password)

            result.fold(
                onSuccess = { user ->
                    _authState.value = AuthState(
                        isLoading = false,
                        isLoggedIn = true,
                        userId = user.id
                    )
                },
                onFailure = { exception ->
                    _authState.value = AuthState(
                        isLoading = false,
                        errorMessage = exception.message
                    )
                }
            )
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)

            val result = userRepository.register(email, password, name)

            result.fold(
                onSuccess = {
                    _authState.value = AuthState(
                        isLoading = false,
                        successMessage = "Registro exitoso. Por favor inicia sesión."
                    )
                },
                onFailure = { exception ->
                    _authState.value = AuthState(
                        isLoading = false,
                        errorMessage = exception.message
                    )
                }
            )
        }
    }

    fun clearMessages() {
        _authState.value = _authState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    fun logout() {
        _authState.value = AuthState()
    }
}