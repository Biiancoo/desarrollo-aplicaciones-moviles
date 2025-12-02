package com.example.app_pasteleria_mil_sabores.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app_pasteleria_mil_sabores.data.dao.UserDao // ✅ import correcto
import com.example.app_pasteleria_mil_sabores.data.dao.ProductDao
import com.example.app_pasteleria_mil_sabores.data.model.Product
import com.example.app_pasteleria_mil_sabores.data.model.User
import android.content.Context

@Database(
    entities = [Product::class, User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao // ✅ ahora apunta a la interfaz en `dao`

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pasteleria_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}