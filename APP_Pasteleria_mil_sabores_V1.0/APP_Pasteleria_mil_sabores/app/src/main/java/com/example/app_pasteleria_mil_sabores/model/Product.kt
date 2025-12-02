package com.example.app_pasteleria_mil_sabores.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val price: Double,

    val description: String,

    // Guarda nombre del recurso en vez del ID para evitar crashes al reinstalar la app
    val imagePath: String = ""
)
