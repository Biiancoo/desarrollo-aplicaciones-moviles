package com.example.app_pasteleria_mil_sabores.data.api.models

data class ApiProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)