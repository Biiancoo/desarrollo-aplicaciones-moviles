package com.example.app_pasteleria_mil_sabores.data.repository

import com.example.app_pasteleria_mil_sabores.data.dao.ProductDao
import com.example.app_pasteleria_mil_sabores.data.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()
    suspend fun insert(product: Product) = productDao.insert(product)
    suspend fun update(product: Product) = productDao.update(product)
    suspend fun delete(product: Product) = productDao.delete(product)
}