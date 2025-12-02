package com.example.app_pasteleria_mil_sabores.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_pasteleria_mil_sabores.data.model.User
import com.example.ecommerce.data.database.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun loadUser(email: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val user = userDao.getUser(email)

                if (user == null) {
                    _uiState.value = ProfileUiState(
                        user = null,
                        isLoading = false,
                        error = "El usuario no existe"
                    )
                } else {
                    _uiState.value = ProfileUiState(
                        user = user,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState(
                    user = null,
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                userDao.update(user) // ← Cambiado a UPDATE (antes: insert)
                _uiState.value = _uiState.value.copy(user = user)
            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(error = "Error al actualizar: ${e.message}")
            }
        }
    }
}
