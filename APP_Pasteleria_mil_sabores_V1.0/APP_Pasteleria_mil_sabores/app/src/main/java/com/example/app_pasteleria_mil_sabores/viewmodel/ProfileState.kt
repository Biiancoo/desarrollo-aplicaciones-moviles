package com.example.app_pasteleria_mil_sabores.viewmodel

data class ProfileState(
    val name: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)