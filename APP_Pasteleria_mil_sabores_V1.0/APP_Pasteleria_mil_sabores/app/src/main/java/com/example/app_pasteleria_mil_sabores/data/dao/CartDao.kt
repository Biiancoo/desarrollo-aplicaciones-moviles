package com.example.app_pasteleria_mil_sabores.data.dao

import androidx.room.*
import com.example.app_pasteleria_mil_sabores.data.entities.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItemEntity)

    @Update
    suspend fun update(cartItem: CartItemEntity)

    @Delete
    suspend fun delete(cartItem: CartItemEntity)

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItems(userId: Int): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId AND userId = :userId LIMIT 1")
    suspend fun getCartItemByProduct(productId: Int, userId: Int): CartItemEntity?

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Int)
}