package com.example.app_pasteleria_mil_sabores.data.dao

import androidx.room.*
import com.example.app_pasteleria_mil_sabores.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUser(email: String): User?
}