package com.example.app_pasteleria_mil_sabores.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // ✅ Room generará automáticamente
    val email: String,
    val password: String,
    val name: String = "",
    val phone: String = "",
    val address: String = ""
)

