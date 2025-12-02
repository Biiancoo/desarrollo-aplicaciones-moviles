package com.example.ecommerce.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app_pasteleria_mil_sabores.data.dao.CartDao
import com.example.app_pasteleria_mil_sabores.data.dao.UserDao
import com.example.app_pasteleria_mil_sabores.data.entities.CartItemEntity
import com.example.app_pasteleria_mil_sabores.data.entities.UserEntity


@Database(
    entities = [UserEntity::class, ProductEntity::class, CartItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ecommerce_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}