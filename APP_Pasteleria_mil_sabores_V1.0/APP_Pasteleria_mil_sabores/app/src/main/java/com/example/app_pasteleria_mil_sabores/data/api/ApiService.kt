package com.example.app_pasteleria_mil_sabores.data.api


import com.example.app_pasteleria_mil_sabores.data.api.models.ApiProduct
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class ApiService {
    private val gson = Gson()
    private val baseUrl = "https://fakestoreapi.com"

    suspend fun getProducts(): List<ApiProduct> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/products")
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                gson.fromJson(response, Array<ApiProduct>::class.java).toList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getProductById(id: Int): ApiProduct? = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/products/$id")
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                gson.fromJson(response, ApiProduct::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}