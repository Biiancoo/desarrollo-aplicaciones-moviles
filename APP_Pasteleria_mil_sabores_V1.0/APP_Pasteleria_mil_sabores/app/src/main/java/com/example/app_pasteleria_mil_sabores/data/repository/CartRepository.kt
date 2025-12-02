package com.example.app_pasteleria_mil_sabores.data.repository

import com.example.app_pasteleria_mil_sabores.data.dao.CartDao
import com.example.app_pasteleria_mil_sabores.data.entities.CartItemEntity
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

    fun getCartItems(userId: Int): Flow<List<CartItemEntity>> {
        return cartDao.getCartItems(userId)
    }

    suspend fun addToCart(
        productId: Int,
        productName: String,
        productPrice: Double,
        userId: Int
    ): Result<Unit> {
        return try {
            val existingItem = cartDao.getCartItemByProduct(productId, userId)
            if (existingItem != null) {
                val updated = existingItem.copy(quantity = existingItem.quantity + 1)
                cartDao.update(updated)
            } else {
                val cartItem = CartItemEntity(
                    productId = productId,
                    productName = productName,
                    productPrice = productPrice,
                    quantity = 1,
                    userId = userId
                )
                cartDao.insert(cartItem)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateQuantity(cartItem: CartItemEntity, newQuantity: Int): Result<Unit> {
        return try {
            if (newQuantity <= 0) {
                cartDao.delete(cartItem)
            } else {
                cartDao.update(cartItem.copy(quantity = newQuantity))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFromCart(cartItem: CartItemEntity): Result<Unit> {
        return try {
            cartDao.delete(cartItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearCart(userId: Int): Result<Unit> {
        return try {
            cartDao.clearCart(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}