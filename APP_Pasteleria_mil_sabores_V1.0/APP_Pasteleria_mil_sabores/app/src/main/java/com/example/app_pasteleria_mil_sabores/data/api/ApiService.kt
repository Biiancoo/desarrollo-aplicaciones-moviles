package com.example.app_pasteleria_mil_sabores.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LoginRequest {
    data class Body(val email: String, val password: String)
    data class Response(val token: String)
}

interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body body: LoginRequest.Body): LoginRequest.Response

    companion object {
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl("https://reqres.in")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}