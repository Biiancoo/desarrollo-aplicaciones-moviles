package com.example.app_pasteleria_mil_sabores.data.repository

import android.content.Context
import android.net.NetworkCapabilities
import android.net.ConnectivityManager
import com.example.app_pasteleria_mil_sabores.data.api.ApiService
import com.example.app_pasteleria_mil_sabores.data.db.AppDatabase
import com.example.app_pasteleria_mil_sabores.data.model.User

class UserRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val userDao = db.userDao()

    suspend fun register(user: User) = userDao.insert(user)

    suspend fun login(email: String, password: String): Result<User> {
        return if (isNetworkAvailable(context)) {
            try {
                ApiService.instance.login(
                    com.example.app_pasteleria_mil_sabores.data.api.LoginRequest.Body(email, password)
                )
                // Si la API responde, guardar usuario local
                val user = User(email, password)
                userDao.insert(user)
                Result.success(user)
            } catch (e: Exception) {
                // Fallback a offline
                tryOfflineLogin(email, password)
            }
        } else {
            tryOfflineLogin(email, password)
        }
    }

    private suspend fun tryOfflineLogin(email: String, password: String): Result<User> {
        val user = userDao.getUser(email)
        return if (user != null && user.password == password) {
            Result.success(user)
        } else {
            Result.failure(IllegalStateException("Credenciales incorrectas"))
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}