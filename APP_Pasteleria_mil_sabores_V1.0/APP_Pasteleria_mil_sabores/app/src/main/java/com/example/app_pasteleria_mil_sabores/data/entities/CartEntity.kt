package com.example.app_pasteleria_mil_sabores.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    val quantity: Int,
    val userId: Int
)