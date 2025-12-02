package com.example.ecommerce.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_pasteleria_mil_sabores.data.entities.CartItemEntity
import com.example.app_pasteleria_mil_sabores.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartState(
    val cartItems: List<CartItemEntity> = emptyList(),
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    fun loadCart(userId: Int) {
        viewModelScope.launch {
            _cartState.value = _cartState.value.copy(isLoading = true)

            cartRepository.getCartItems(userId).collect { items ->
                val total = items.sumOf { it.productPrice * it.quantity }
                _cartState.value = CartState(
                    cartItems = items,
                    totalAmount = total,
                    isLoading = false
                )
            }
        }
    }

    fun addToCart(productId: Int, productName: String, productPrice: Double, userId: Int) {
        viewModelScope.launch {
            val result = cartRepository.addToCart(productId, productName, productPrice, userId)

            result.fold(
                onSuccess = {
                    _cartState.value = _cartState.value.copy(
                        successMessage = "Producto agregado al carrito"
                    )
                },
                onFailure = { exception ->
                    _cartState.value = _cartState.value.copy(
                        errorMessage = exception.message
                    )
                }
            )
        }
    }

    fun updateQuantity(cartItem: CartItemEntity, newQuantity: Int) {
        viewModelScope.launch {
            val result = cartRepository.updateQuantity(cartItem, newQuantity)

            result.fold(
                onSuccess = { },
                onFailure = { exception ->
                    _cartState.value = _cartState.value.copy(
                        errorMessage = exception.message
                    )
                }
            )
        }
    }

    fun removeFromCart(cartItem: CartItemEntity) {
        viewModelScope.launch {
            val result = cartRepository.removeFromCart(cartItem)

            result.fold(
                onSuccess = {
                    _cartState.value = _cartState.value.copy(
                        successMessage = "Producto eliminado del carrito"
                    )
                },
                onFailure = { exception ->
                    _cartState.value = _cartState.value.copy(
                        errorMessage = exception.message
                    )
                }
            )
        }
    }

    fun clearCart(userId: Int) {
        viewModelScope.launch {
            val result = cartRepository.clearCart(userId)

            result.fold(
                onSuccess = {
                    _cartState.value = _cartState.value.copy(
                        successMessage = "Carrito limpiado"
                    )
                },
                onFailure = { exception ->
                    _cartState.value = _cartState.value.copy(
                        errorMessage = exception.message
                    )
                }
            )
        }
    }

    fun clearMessages() {
        _cartState.value = _cartState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}