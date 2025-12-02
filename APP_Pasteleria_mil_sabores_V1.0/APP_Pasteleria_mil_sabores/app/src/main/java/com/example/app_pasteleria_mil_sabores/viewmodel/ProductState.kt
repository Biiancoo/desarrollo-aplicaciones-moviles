package com.example.app_pasteleria_mil_sabores.viewmodel

import com.example.app_pasteleria_mil_sabores.model.Product

// ProductState.kt
data class ProductState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)
