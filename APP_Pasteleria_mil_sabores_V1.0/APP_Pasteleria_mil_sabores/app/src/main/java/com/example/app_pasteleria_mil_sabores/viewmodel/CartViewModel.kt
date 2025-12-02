package com.example.app_pasteleria_mil_sabores.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_pasteleria_mil_sabores.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {
    private val _cart = MutableStateFlow<List<Product>>(emptyList())
    val cart: StateFlow<List<Product>> = _cart

    fun addToCart(product: Product) {
        val current = _cart.value
        if (!current.any { it.id == product.id }) {
            _cart.value = current + product
        }
    }

    fun removeFromCart(product: Product) {
        _cart.value = _cart.value.filter { it.id != product.id }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    val totalPrice: Double
        get() = cart.value.sumOf { it.price }
}