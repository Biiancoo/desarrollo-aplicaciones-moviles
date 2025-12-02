package com.example.app_pasteleria_mil_sabores.viewmodel

import com.example.app_pasteleria_mil_sabores.data.entities.ProductEntity
import com.example.app_pasteleria_mil_sabores.model.Product


data class ProductState(
    val products2: List<ProductEntity>,
    val products1: List<ProductEntity>,
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null,
    val currentProduct: ProductEntity?,
    val userId: Int
)
